package lgbt.mystic.syscan.steps.files

import lgbt.mystic.syscan.PlatformCreateTempDir
import lgbt.mystic.syscan.PlatformPath
import lgbt.mystic.syscan.PlatformProcessSpawner
import lgbt.mystic.syscan.artifact.AnalysisContext
import lgbt.mystic.syscan.artifact.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.artifact.ExtractableArtifact
import lgbt.mystic.syscan.dyld.IpswSharedCacheDylib
import lgbt.mystic.syscan.dyld.IpswSharedCacheList
import lgbt.mystic.syscan.io.FsPath
import lgbt.mystic.syscan.io.deleteRecursively
import lgbt.mystic.syscan.metadata.*
import kotlinx.serialization.json.Json
import lgbt.mystic.syscan.metadata.keys.FileMetadataKeys

object IpswAnalysisStep : AnalysisStep {
  override val wants: MetadataWants = listOf(FileMetadataKeys.ReadableFilePath.want())
  override val provides: MetadataKeys = listOf()

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    val path = artifact.metadata.require(FileMetadataKeys.ReadableFilePath)
    if (!path.fullPathString.startsWith("/System/Library/dyld/") ||
          !path.entityNameString.startsWith("dyld_shared_cache_") ||
        path.fullPathString.contains(".")) {
      return
    }

    val result = PlatformProcessSpawner.execute("ipsw", listOf(
      "dyld",
      "info",
      "-l",
      "-j",
      path.fullPathString
    ))

    if (result.exitCode != 0) {
      return
    }

    val jsonContentString = result.stdoutAsString
    val sharedCacheList = json.decodeFromString(IpswSharedCacheList.serializer(), jsonContentString)
    for (dylib in sharedCacheList.dylibs) {
      val virtualFilePath = PlatformPath(dylib.name)
      val dylibArtifact = IpswDyldArtifact(path, sharedCacheList.magic.split(" ").last(), dylib, virtualFilePath)
      dylibArtifact.prepare(this)
      context.emit(dylibArtifact)
    }
  }

  class IpswDyldArtifact(val sharedCachePath: FsPath, sharedCacheArchitecture: String, val dylib: IpswSharedCacheDylib, virtualFilePath: FsPath) :
    ExtractableArtifact("$sharedCacheArchitecture:${dylib.name}", virtualFilePath) {
    lateinit var temporaryDirectory: FsPath

    override fun extractToFile(): FsPath {
      temporaryDirectory = PlatformCreateTempDir()
      val executableFilePath = temporaryDirectory.resolve(virtualFilePath.entityNameString)
      val result = PlatformProcessSpawner.execute("ipsw", listOf(
        "dyld",
        "macho",
        "-x",
        sharedCachePath.fullPathString,
        dylib.name,
        "--output",
        temporaryDirectory.fullPathString
      ))

      if (result.exitCode != 0) {
        throw RuntimeException(result.stderrAsString)
      }

      return executableFilePath
    }

    override fun cleanup() {
      super.cleanup()
      temporaryDirectory.deleteRecursively()
    }
  }

  private val json = Json {
    ignoreUnknownKeys = true
  }
}
