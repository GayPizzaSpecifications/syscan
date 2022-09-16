package lgbt.mystic.syscan.steps.systems

import lgbt.mystic.syscan.analysis.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.artifact.ArtifactKinds

interface SystemAnalysisStep : AnalysisStep {
  override fun valid(artifact: Artifact): Boolean =
    artifact.metadata.kind == ArtifactKinds.System
}
