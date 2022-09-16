package lgbt.mystic.syscan.frontend

import lgbt.mystic.syscan.analysis.AnalysisContext
import lgbt.mystic.syscan.analysis.AnalysisStep
import lgbt.mystic.syscan.analysis.AnalysisSteps
import lgbt.mystic.syscan.analysis.filterSupportedSteps
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.artifact.FileArtifact
import lgbt.mystic.syscan.io.*
import lgbt.mystic.syscan.metadata.MetadataSourcePlanner
import lgbt.mystic.syscan.metadata.satisfiesMetadataWantsOf
import lgbt.mystic.syscan.pipeline.PooledPipeline
import lgbt.mystic.syscan.pipeline.SimplePipeline
import lgbt.mystic.syscan.system.AnalysisSystem

class SystemAnalyzer(val configuration: SystemAnalyzerConfiguration) {
  private val pipeline: PooledPipeline<Artifact>
  private val visitor: ArtifactPathVisitor

  val context: AnalysisContext

  init {
    val planner = MetadataSourcePlanner(AnalysisSteps.all.filterSupportedSteps())
    val steps = planner.plan()

    pipeline = PooledPipeline(SimplePipeline(), configuration.taskPool)
    visitor = ArtifactPathVisitor { artifact ->
      pipeline.emit(artifact)
    }

    context = object : AnalysisContext {
      override fun emit(artifact: Artifact) {
        if (!configuration.handler.acceptContextEmit(artifact)) {
          return
        }

        pipeline.emit(artifact)
      }

      override fun scan(path: FsPath) {
        if (!configuration.handler.acceptContextRootScan(path)) {
          return
        }

        pipeline.pool.submit {
          path.visit(visitor)
        }
      }
    }

    pipeline.addHandler { artifact: Artifact ->
      handleArtifact(artifact, context, steps)
    }
  }

  fun submitScanRoot(path: FsPath) {
    configuration.taskPool.submit {
      path.visit(visitor)
    }
  }

  fun submitLocalSystem() {
    val localSystem = AnalysisSystem("local-system")
    pipeline.emit(localSystem)
  }

  fun submit(artifact: Artifact) {
    pipeline.emit(artifact)
  }

  private fun handleArtifact(artifact: Artifact, context: AnalysisContext, steps: List<AnalysisStep>) {
    configuration.handler.onArtifactStart(artifact)

    for (step in steps) {
      if (!configuration.handler.shouldRunStepOnArtifact(step, artifact)) {
        continue
      }

      try {
        if (artifact.satisfiesMetadataWantsOf(step) && step.valid(artifact)) {
          step.analyze(context, artifact)
        }
      } catch (e: Exception) {
        configuration.handler.onArtifactStepError(artifact, step, e)
      }
    }

    configuration.handler.onArtifactEnd(artifact)

    artifact.cleanup()
  }

  private class ArtifactPathVisitor(val block: (Artifact) -> Unit) : FsPathVisitor {
    override fun beforeVisitDirectory(path: FsPath): FsPathVisitor.VisitResult {
      return FsPathVisitor.VisitResult.Continue
    }

    override fun visitFile(path: FsPath): FsPathVisitor.VisitResult {
      if (path.isExecutable() && path.isRegularFile()) {
        block(FileArtifact(path))
      }
      return FsPathVisitor.VisitResult.Continue
    }

    override fun visitFileFailed(path: FsPath, exception: Exception): FsPathVisitor.VisitResult {
      return FsPathVisitor.VisitResult.Continue
    }

    override fun afterVisitDirectory(path: FsPath): FsPathVisitor.VisitResult {
      return FsPathVisitor.VisitResult.Continue
    }
  }
}
