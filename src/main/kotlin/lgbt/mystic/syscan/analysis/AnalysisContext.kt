package lgbt.mystic.syscan.analysis

import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.io.FsPath

interface AnalysisContext {
  fun emit(artifact: Artifact)
  fun scan(path: FsPath)
}
