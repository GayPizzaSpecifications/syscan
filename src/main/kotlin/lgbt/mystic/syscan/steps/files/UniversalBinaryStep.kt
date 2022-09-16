package lgbt.mystic.syscan.steps.files

import lgbt.mystic.syscan.PlatformProcessSpawner
import lgbt.mystic.syscan.analysis.AnalysisContext
import lgbt.mystic.syscan.analysis.AnalysisRequirements
import lgbt.mystic.syscan.analysis.requirements.OperatingSystem
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.metadata.MetadataKeys
import lgbt.mystic.syscan.metadata.MetadataWants
import lgbt.mystic.syscan.metadata.keys.FileMetadataKeys

object UniversalBinaryStep : FileAnalysisStep {
  override val wants: MetadataWants = listOf(FileMetadataKeys.ReadableFilePath.want(), FileMetadataKeys.MimeType.want())
  override val provides: MetadataKeys = listOf(FileMetadataKeys.UniversalBinaryArchitectures)

  override fun required(requirements: AnalysisRequirements): Unit = requirements.run {
    os(OperatingSystem.MacOS)
    executable("lipo")
  }

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
