package io.kexec.syscan.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import io.kexec.syscan.artifact.*
import io.kexec.syscan.metadata.MetadataStore
import io.kexec.syscan.metadata.hasMetadataWants
import io.kexec.syscan.concurrent.TaskPool
import io.kexec.syscan.io.*
import io.kexec.syscan.io.java.toJavaPath
import io.kexec.syscan.metadata.MetadataSourcePlanner
import io.kexec.syscan.pipeline.PooledPipeline
import kotlinx.serialization.json.Json
import java.io.FileOutputStream
import java.io.PrintStream
import kotlin.io.path.outputStream

class AnalyzeFiles : CliktCommand("Artifact Discovery", name = "analyze-files") {
  private val roots by option("--root", "-r").fsPath(mustExist = true, canBeFile = false).multiple()
  private val outputFilePath by option("--output-file-path", "-o").fsPath()
  private val skipStepList by option("--skip-step", "-S").multiple()

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

    val context = object : AnalysisContext {
      override fun emit(artifact: Artifact) {
        pipeline.emit(artifact)
      }
    }

    pipeline.addHandler { artifact: Artifact ->
      handleArtifact(artifact, context, steps)
    }

    val visitor = ArtifactPathVisitor { artifact ->
      pipeline.emit(artifact)
    }

    roots.forEach { root ->
      root.visit(visitor)
    }
    pool.waitAndStop()
  }

  fun handleArtifact(artifact: Artifact, context: AnalysisContext, steps: List<AnalysisStep>) {
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

  fun encodeMetadata(store: MetadataStore): String =
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
