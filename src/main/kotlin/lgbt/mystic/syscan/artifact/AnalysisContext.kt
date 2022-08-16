package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.io.FsPath

interface AnalysisContext {
  fun emit(artifact: Artifact)
  fun scan(path: FsPath)
}
