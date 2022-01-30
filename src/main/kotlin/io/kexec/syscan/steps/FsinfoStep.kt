package io.kexec.syscan.steps

import io.kexec.syscan.artifact.AnalysisStep
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.artifact.FileArtifact
import io.kexec.syscan.common.MetadataKey
import io.kexec.syscan.common.MetadataKeys
import io.kexec.syscan.io.isExecutable
import kotlinx.serialization.json.JsonPrimitive

class FsinfoStep : AnalysisStep {
  override val wants = emptyList<MetadataKey<*>>()
  override val provides = listOf(MetadataKeys.FilePath, MetadataKeys.ExecutableFileMarker)

  override fun analyze(artifact: Artifact) {
    if (artifact !is FileArtifact) {
      return
    }
    artifact.metadata.set(this, MetadataKeys.FilePath, artifact.path) { JsonPrimitive(it.fullPathString) }
    artifact.metadata.set(this, MetadataKeys.ExecutableFileMarker, artifact.path.isExecutable())
  }
}
