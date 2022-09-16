package lgbt.mystic.syscan.analysis

import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.metadata.MetadataSource

interface AnalysisStep : MetadataSource {
  fun required(requirements: AnalysisRequirements) {}

  fun supported(): Boolean = AnalysisRequirements().let { requirements ->
    required(requirements)
    requirements
  }.satisfied()

  fun valid(artifact: Artifact): Boolean = true
  fun analyze(context: AnalysisContext, artifact: Artifact)
}
