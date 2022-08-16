package lgbt.mystic.syscan.tool

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import lgbt.mystic.syscan.artifact.*
import lgbt.mystic.syscan.concurrent.TaskPool
import lgbt.mystic.syscan.io.*
import lgbt.mystic.syscan.io.java.toJavaPath
import lgbt.mystic.syscan.metadata.MetadataSourcePlanner
import lgbt.mystic.syscan.metadata.MetadataStore
import lgbt.mystic.syscan.metadata.hasMetadataWants
import lgbt.mystic.syscan.pipeline.PooledPipeline
import lgbt.mystic.syscan.system.CurrentSystem
import java.io.PrintStream
import java.nio.file.Path
import kotlin.io.path.outputStream

class AnalyzerTool : CliktCommand("System Analyzer", name = "analyze") {
  private val roots by option("--root", "-r").fsPath(mustExist = true, canBeFile = false).multiple()
  private val outputFilePath by option("--output-file-path", "-o").fsPath()
  private val skipStepList by option("--skip-step", "-S").multiple()

  private val restrictive by option("--restrictive", "-R").flag()

  private val pool by requireObject<TaskPool>()

  lateinit var outputPrintStream: PrintStream

  override fun run() {
    val planner = MetadataSourcePlanner(AnalysisSteps.all)
    val steps = planner.plan()

    if (outputFilePath != null && outputFilePath!!.exists()) {
      outputFilePath!!.delete()
    }

    val outputFileStream = outputFilePath?.toJavaPath()?.outputStream()
    outputPrintStream = if (outputFileStream != null) PrintStream(outputFileStream) else System.out

    val pipeline = PooledPipeline<Artifact>(pool)

    val visitor = ArtifactPathVisitor { artifact ->
      pipeline.emit(artifact)
    }

    val context = object : AnalysisContext {
      override fun emit(artifact: Artifact) {
        if (restrictive) {
          return
        }

        pipeline.emit(artifact)
      }

      override fun scan(path: FsPath) {
        if (restrictive) {
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

    roots.forEach { root ->
      pool.submit {
        root.visit(visitor)
      }
    }

    pipeline.emit(CurrentSystem)

    pool.waitAndStop()
  }

  private fun handleArtifact(artifact: Artifact, context: AnalysisContext, steps: List<AnalysisStep>) {
    for (step in steps) {
      if (skipStepList.contains(step.metadataSourceKey)) {
        continue
      }

      try {
        if (artifact.hasMetadataWants(step)) {
          step.analyze(context, artifact)
        }
      } catch (e: Exception) {
        System.err.println("ERROR while analyzing artifact $artifact in step ${step.metadataSourceKey}")
        e.printStackTrace()
      }
    }

    val encoded = encodeMetadata(artifact.metadata)
    synchronized(outputPrintStream) {
      outputPrintStream.println(encoded)
    }

    artifact.cleanup()
  }

  private fun encodeMetadata(store: MetadataStore): String =
    store.encodeToJson().toString()

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
