package io.kexec.syscan.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import io.kexec.syscan.artifact.AnalysisSteps
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.artifact.FileArtifact
import io.kexec.syscan.common.MetadataStore
import io.kexec.syscan.concurrent.ConcurrentPool
import io.kexec.syscan.io.fsPath
import io.kexec.syscan.io.*
import io.kexec.syscan.pipeline.MetadataSourcePlanner
import kotlinx.serialization.json.Json

class AnalyzeFiles : CliktCommand("Artifact Discovery", name = "analyze-files") {
  private val rootFsPath by option("--root", "-r").fsPath(mustExist = true, canBeFile = false).required()

  private val pool by requireObject<ConcurrentPool>()

  override fun run() {
    val planner = MetadataSourcePlanner(AnalysisSteps.all)
    val steps = planner.plan()

    val encodeMetadata = { store: MetadataStore ->
      Json.encodeToString(MetadataStore.serializer(), store)
    }

    val visitor = ArtifactPathVisitor { artifact ->
      pool.submit {
        for (step in steps) {
          step.analyze(artifact)
        }
        println(encodeMetadata(artifact.metadata))
      }
    }
    rootFsPath.visit(visitor)
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
