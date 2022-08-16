package lgbt.mystic.syscan.steps

import lgbt.mystic.syscan.PlatformProcessSpawner
import lgbt.mystic.syscan.artifact.AnalysisContext
import lgbt.mystic.syscan.artifact.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.metadata.*

object MagicFileStep : AnalysisStep {
  override val wants: MetadataWants = listOf(CommonMetadataKeys.ReadableFilePath.want())
  override val provides: MetadataKeys = listOf(CommonMetadataKeys.MimeType)

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    val path = artifact.metadata.require(CommonMetadataKeys.ReadableFilePath)
    val result = PlatformProcessSpawner.execute("file", listOf(
      "--brief",
      "--mime-type",
      path.fullPathString
    ))

    if (result.exitCode != 0) {
      return
    }

    val mime = result.stdoutAsString.lines().first().trim()
    artifact.metadata.set(this, CommonMetadataKeys.MimeType, mime)
  }
}
