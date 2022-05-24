package io.kexec.syscan.steps

import io.kexec.syscan.artifact.AnalysisContext
import io.kexec.syscan.artifact.AnalysisStep
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.artifact.FileArtifact
import io.kexec.syscan.metadata.CommonMetadataKeys
import io.kexec.syscan.io.isExecutable
import io.kexec.syscan.io.lastModifiedTime
import io.kexec.syscan.metadata.MetadataKeys
import io.kexec.syscan.metadata.MetadataWants

object FileInfoStep : AnalysisStep {
  override val wants: MetadataWants = emptyList()
  override val provides: MetadataKeys = listOf(CommonMetadataKeys.ReadableFilePath, CommonMetadataKeys.ExecutableFileMarker)

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    if (artifact !is FileArtifact) {
      return
    }
    artifact.metadata.set(this, CommonMetadataKeys.ReadableFilePath, artifact.path)
    artifact.metadata.set(this, CommonMetadataKeys.VirtualFilePath, artifact.path)
    artifact.metadata.set(this, CommonMetadataKeys.ExecutableFileMarker, artifact.path.isExecutable())
    artifact.metadata.set(this, CommonMetadataKeys.LastModified, artifact.path.lastModifiedTime())
  }
}
