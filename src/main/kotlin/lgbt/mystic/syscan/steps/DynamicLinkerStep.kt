package lgbt.mystic.syscan.steps

import lgbt.mystic.syscan.PlatformPath
import lgbt.mystic.syscan.PlatformProcessSpawner
import lgbt.mystic.syscan.artifact.AnalysisContext
import lgbt.mystic.syscan.artifact.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.io.FsPathSerializer
import lgbt.mystic.syscan.metadata.*

object DynamicLinkerStep : AnalysisStep {
  override val wants: MetadataWants = listOf(CommonMetadataKeys.ReadableFilePath.want(), CommonMetadataKeys.MimeType.want())
  override val provides: MetadataKeys = listOf(CommonMetadataKeys.DynamicLinkerLinkedFiles)

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    val mime = artifact.metadata.require(CommonMetadataKeys.MimeType)
    if (mime != MachBinaryType) {
      return
    }

    val path = artifact.metadata.require(CommonMetadataKeys.ReadableFilePath)

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
      CommonMetadataKeys.DynamicLinkerLinkedFiles,
      FsPathSerializer,
      linkedFiles)

    val linkedFrameworks = linkedFiles.asSequence().filter { it.fullPathString.contains(".framework/") }
      .map { it.fullPathString.split(".framework/").first() + ".framework" }
      .map { PlatformPath(it) }
      .distinct()
      .sortedBy { it.fullPathString }.toList()

    artifact.metadata.setList(
      this,
      CommonMetadataKeys.DynamicLinkerLinkedFrameworks,
      FsPathSerializer,
      linkedFrameworks
    )
  }

  private const val MachBinaryType = "application/x-mach-binary"
}
