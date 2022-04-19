package io.kexec.syscan.steps

import io.kexec.syscan.PlatformProcessSpawner
import io.kexec.syscan.artifact.AnalysisStep
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.metadata.MetadataKey
import io.kexec.syscan.metadata.MetadataKeys

object MagicFileStep : AnalysisStep {
  override val wants: List<MetadataKey<*>> = listOf(MetadataKeys.FilePath)
  override val provides: List<MetadataKey<*>> = listOf(MetadataKeys.MimeType)

  override fun analyze(artifact: Artifact) {
    val path = artifact.metadata.require(MetadataKeys.FilePath)
    val result = PlatformProcessSpawner.execute("file", listOf(
      "--brief",
      "--mime-type",
      path.fullPathString
    ))

    if (result.exitCode != 0) {
      return
    }

    val mime = result.stdoutAsString.lines().first().trim()
    artifact.metadata.set(this, MetadataKeys.MimeType, mime)
  }
}
