package io.kexec.syscan.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.artifact.ExecutableFileArtifact
import io.kexec.syscan.common.AttributedData
import io.kexec.syscan.io.fsPath
import io.kexec.syscan.io.*
import io.kexec.syscan.pipeline.Pipeline
import kotlinx.serialization.json.Json

class ArtifactDiscoveryCommand : CliktCommand("Artifact Discovery", name = "discover-artifacts") {
  private val rootFsPath by option("--root", "-r").fsPath(mustExist = true, canBeFile = false).required()

  override fun run() {
    val pipeline = Pipeline(Artifact::class)
    pipeline.addHandler { artifact ->
      val description = artifact.describe()
      val content = Json.encodeToString(AttributedData.serializer(), description)
      println(content)
    }

    val visitor = ArtifactPathVisitor(pipeline::pipe)
    rootFsPath.visit(visitor)
  }

  private class ArtifactPathVisitor(val block: (Artifact) -> Unit) : FsPathVisitor {
    override fun beforeVisitDirectory(path: FsPath): FsPathVisitor.VisitResult {
      return FsPathVisitor.VisitResult.Continue
    }

    override fun visitFile(path: FsPath): FsPathVisitor.VisitResult {
      if (path.isExecutable() && path.isRegularFile()) {
        block(ExecutableFileArtifact(path))
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
