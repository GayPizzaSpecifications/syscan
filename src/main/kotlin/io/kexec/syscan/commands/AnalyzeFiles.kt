package io.kexec.syscan.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import io.kexec.syscan.artifact.AnalysisContext
import io.kexec.syscan.artifact.AnalysisSteps
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.artifact.FileArtifact
import io.kexec.syscan.metadata.MetadataStore
import io.kexec.syscan.metadata.hasMetadataWants
import io.kexec.syscan.concurrent.TaskPool
import io.kexec.syscan.io.*
import io.kexec.syscan.io.java.toJavaPath
import io.kexec.syscan.metadata.MetadataSourcePlanner
import kotlinx.serialization.json.Json
import java.io.PrintStream
import kotlin.io.path.outputStream

class AnalyzeFiles : CliktCommand("Artifact Discovery", name = "analyze-files") {
  private val roots by option("--root", "-r").fsPath(mustExist = true, canBeFile = false).multiple()
  private val outputFilePath by option("--output-file-path", "-o").fsPath()
  private val skipStepList by option("--skip-step", "-S").multiple()

  private val pool by requireObject<TaskPool>()

  override fun run() {
    val planner = MetadataSourcePlanner(AnalysisSteps.all)
    val steps = planner.plan()

    val encodeMetadata = { store: MetadataStore ->
      store.encodeToJson().toString()
    }

    if (outputFilePath != null && outputFilePath!!.exists()) {
      outputFilePath!!.delete()
    }

    val outputFileStream = outputFilePath?.toJavaPath()?.outputStream()
    val outputPrintStream = if (outputFileStream != null) PrintStream(outputFileStream) else System.out

    var analyze: ((Artifact) -> Unit)? = null

    val context = object : AnalysisContext {
      override fun emit(artifact: Artifact) {
        pool.submit {
          analyze!!(artifact)
        }
      }
    }

    analyze = { artifact: Artifact ->
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
      if (outputPrintStream != null) {
        synchronized(outputPrintStream) {
          outputPrintStream.println(encoded)
        }
      }

      artifact.cleanup()
    }

    val visitor = ArtifactPathVisitor { artifact ->
      pool.submit {
        analyze(artifact)
      }
    }

    roots.forEach { root ->
      root.visit(visitor)
    }
    pool.waitAndStop()
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
