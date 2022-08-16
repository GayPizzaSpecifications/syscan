package lgbt.mystic.syscan.steps.files

import lgbt.mystic.syscan.PlatformPath
import lgbt.mystic.syscan.PlatformProcessSpawner
import lgbt.mystic.syscan.artifact.AnalysisContext
import lgbt.mystic.syscan.artifact.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.io.FsPathSerializer
import lgbt.mystic.syscan.metadata.*
import lgbt.mystic.syscan.metadata.keys.FileMetadataKeys

object DynamicLinkerStep : AnalysisStep {
  override val wants: MetadataWants = listOf(FileMetadataKeys.ReadableFilePath.want(), FileMetadataKeys.MimeType.want())
  override val provides: MetadataKeys = listOf(FileMetadataKeys.DynamicLinkerLinkedFiles)

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    val mime = artifact.metadata.require(FileMetadataKeys.MimeType)
    if (mime != MachBinaryType) {
      return
    }

    val path = artifact.metadata.require(FileMetadataKeys.ReadableFilePath)

    val result = PlatformProcessSpawner.execute(
      "otool", listOf(
        "-L",
        path.fullPathString
      )
    )

    if (result.exitCode != 0) {
      return
    }
    val lines = result.stdoutAsString.lines()
    val linkedLibraryLines = lines.filter { it.startsWith("\t") }.map { it.trim() }
    val linkedFiles = linkedLibraryLines.asSequence()
      .map { it.split("(").first().trim() }
      .distinct()
      .map { PlatformPath(it) }
      .sortedBy { it.fullPathString }
      .toList()

    artifact.metadata.setList(
      this,
      FileMetadataKeys.DynamicLinkerLinkedFiles,
      FsPathSerializer,
      linkedFiles)

    val linkedFrameworks = linkedFiles.asSequence().filter { it.fullPathString.contains(".framework/") }
      .map { it.fullPathString.split(".framework/").first() + ".framework" }
      .map { PlatformPath(it) }
      .distinct()
      .sortedBy { it.fullPathString }.toList()

    artifact.metadata.setList(
      this,
      FileMetadataKeys.DynamicLinkerLinkedFrameworks,
      FsPathSerializer,
      linkedFrameworks
    )
  }

  private const val MachBinaryType = "application/x-mach-binary"
}
