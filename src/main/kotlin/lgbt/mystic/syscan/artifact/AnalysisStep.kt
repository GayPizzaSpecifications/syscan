package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.metadata.MetadataSource

interface AnalysisStep : MetadataSource {
  fun analyze(context: AnalysisContext, artifact: Artifact)
}
