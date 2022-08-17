package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.metadata.MetadataSource

interface AnalysisStep : MetadataSource {
  fun valid(artifact: Artifact): Boolean = true
  fun analyze(context: AnalysisContext, artifact: Artifact)
}
