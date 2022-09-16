package lgbt.mystic.syscan.frontend

import lgbt.mystic.syscan.analysis.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.io.FsPath

object NullSystemAnalyzerHandler : SystemAnalyzerHandler {
  override fun onArtifactStart(artifact: Artifact) {}
  override fun onArtifactEnd(artifact: Artifact) {}
  override fun onArtifactStepError(artifact: Artifact, step: AnalysisStep, e: Throwable) {}

  override fun shouldRunStepOnArtifact(step: AnalysisStep, artifact: Artifact): Boolean = true
  override fun acceptContextEmit(artifact: Artifact): Boolean = true
  override fun acceptContextRootScan(path: FsPath): Boolean = true
}
