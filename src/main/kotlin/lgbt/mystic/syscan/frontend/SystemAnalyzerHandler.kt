package lgbt.mystic.syscan.frontend

import lgbt.mystic.syscan.analysis.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.io.FsPath

interface SystemAnalyzerHandler {
  fun onArtifactStart(artifact: Artifact)
  fun onArtifactEnd(artifact: Artifact)
  fun onArtifactStepError(artifact: Artifact, step: AnalysisStep, e: Throwable)

  fun shouldRunStepOnArtifact(step: AnalysisStep, artifact: Artifact): Boolean

  fun acceptContextEmit(artifact: Artifact): Boolean
  fun acceptContextRootScan(path: FsPath): Boolean
}
