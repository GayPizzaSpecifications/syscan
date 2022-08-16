package lgbt.mystic.syscan.steps.files

import lgbt.mystic.syscan.PlatformProcessSpawner
import lgbt.mystic.syscan.artifact.AnalysisContext
import lgbt.mystic.syscan.artifact.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.metadata.*
import lgbt.mystic.syscan.metadata.keys.FileMetadataKeys

object UniversalBinaryStep : AnalysisStep {
  override val wants: MetadataWants = listOf(FileMetadataKeys.ReadableFilePath.want(), FileMetadataKeys.MimeType.want())
  override val provides: MetadataKeys = listOf(FileMetadataKeys.UniversalBinaryArchitectures)

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    val mimeType = artifact.metadata.require(FileMetadataKeys.MimeType)
    if (mimeType != MachBinaryType) {
      return
    }

    val path = artifact.metadata.require(FileMetadataKeys.ReadableFilePath)
    val result = PlatformProcessSpawner.execute("lipo", listOf(
      "-info",
      path.fullPathString
    ))

    if (result.exitCode != 0) {
      return
    }

    val architectureListLine = result.stdoutAsString.lines().first()
    val architectureSection = architectureListLine.split(": ").last().trim()
    val architectures = architectureSection.split(" ").map { it.trim() }.sorted()
    artifact.metadata.set(this, FileMetadataKeys.UniversalBinaryArchitectures, architectures)
  }

  private const val MachBinaryType = "application/x-mach-binary"
}
