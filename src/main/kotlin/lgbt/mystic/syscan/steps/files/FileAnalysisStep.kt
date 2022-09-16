package lgbt.mystic.syscan.steps.files

import lgbt.mystic.syscan.analysis.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.artifact.ArtifactKinds

interface FileAnalysisStep : AnalysisStep {
  override fun valid(artifact: Artifact): Boolean =
    artifact.metadata.kind == ArtifactKinds.File
}
