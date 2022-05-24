package io.kexec.syscan.steps

import io.kexec.syscan.PlatformCreateTempDir
import io.kexec.syscan.PlatformPath
import io.kexec.syscan.PlatformProcessSpawner
import io.kexec.syscan.artifact.AnalysisContext
import io.kexec.syscan.artifact.AnalysisStep
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.artifact.ExtractableArtifact
import io.kexec.syscan.dyld.IpswSharedCacheDylib
import io.kexec.syscan.dyld.IpswSharedCacheList
import io.kexec.syscan.io.FsPath
import io.kexec.syscan.io.deleteRecursively
import io.kexec.syscan.metadata.*
import kotlinx.serialization.json.Json

object IpswAnalysisStep : AnalysisStep {
  override val wants: MetadataWants = listOf(CommonMetadataKeys.ReadableFilePath.want())
  override val provides: MetadataKeys = listOf()

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    val path = artifact.metadata.require(CommonMetadataKeys.ReadableFilePath)
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
