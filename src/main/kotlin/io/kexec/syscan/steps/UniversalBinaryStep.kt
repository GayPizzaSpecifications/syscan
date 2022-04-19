package io.kexec.syscan.steps

import io.kexec.syscan.PlatformProcessSpawner
import io.kexec.syscan.artifact.AnalysisStep
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.metadata.MetadataKey
import io.kexec.syscan.metadata.MetadataKeys

object UniversalBinaryStep : AnalysisStep {
  override val wants: List<MetadataKey<*>> = listOf(MetadataKeys.FilePath, MetadataKeys.ExecutableFileMarker, MetadataKeys.MimeType)
  override val provides: List<MetadataKey<*>> = listOf(MetadataKeys.UniversalBinaryArchitectures)

  override fun analyze(artifact: Artifact) {
    val mimeType = artifact.metadata.require(MetadataKeys.MimeType)
    if (mimeType != MachBinaryType) {
      return
    }

    val path = artifact.metadata.require(MetadataKeys.FilePath)
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
    artifact.metadata.set(this, MetadataKeys.UniversalBinaryArchitectures, architectures)
  }

  private const val MachBinaryType = "application/x-mach-binary"
}
