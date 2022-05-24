package io.kexec.syscan.steps

import io.kexec.syscan.PlatformProcessSpawner
import io.kexec.syscan.artifact.AnalysisContext
import io.kexec.syscan.artifact.AnalysisStep
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.metadata.*

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
