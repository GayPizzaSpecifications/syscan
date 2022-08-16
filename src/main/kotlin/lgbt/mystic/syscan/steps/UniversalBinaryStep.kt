package lgbt.mystic.syscan.steps

import lgbt.mystic.syscan.PlatformProcessSpawner
import lgbt.mystic.syscan.artifact.AnalysisContext
import lgbt.mystic.syscan.artifact.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.metadata.*

object UniversalBinaryStep : AnalysisStep {
  override val wants: MetadataWants = listOf(CommonMetadataKeys.ReadableFilePath.want(), CommonMetadataKeys.MimeType.want())
  override val provides: MetadataKeys = listOf(CommonMetadataKeys.UniversalBinaryArchitectures)

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    val mimeType = artifact.metadata.require(CommonMetadataKeys.MimeType)
    if (mimeType != MachBinaryType) {
      return
    }

    val path = artifact.metadata.require(CommonMetadataKeys.ReadableFilePath)
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
    artifact.metadata.set(this, CommonMetadataKeys.UniversalBinaryArchitectures, architectures)
  }

  private const val MachBinaryType = "application/x-mach-binary"
}
