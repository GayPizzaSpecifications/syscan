package lgbt.mystic.syscan.steps

import lgbt.mystic.syscan.artifact.AnalysisContext
import lgbt.mystic.syscan.artifact.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.artifact.FileArtifact
import lgbt.mystic.syscan.metadata.CommonMetadataKeys
import lgbt.mystic.syscan.io.isExecutable
import lgbt.mystic.syscan.io.lastModifiedTime
import lgbt.mystic.syscan.metadata.MetadataKeys
import lgbt.mystic.syscan.metadata.MetadataWants

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
