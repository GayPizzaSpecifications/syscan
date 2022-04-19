package io.kexec.syscan.steps

import io.kexec.syscan.PlatformPath
import io.kexec.syscan.PlatformProcessSpawner
import io.kexec.syscan.artifact.AnalysisStep
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.io.FsPathSerializer
import io.kexec.syscan.metadata.MetadataKey
import io.kexec.syscan.metadata.MetadataKeys

object DynamicLinkerStep : AnalysisStep {
  override val wants: List<MetadataKey<*>> = listOf(MetadataKeys.FilePath)
  override val provides: List<MetadataKey<*>> = listOf(MetadataKeys.DynamicLinkerLinkedFiles)

  override fun analyze(artifact: Artifact) {
    val path = artifact.metadata.require(MetadataKeys.FilePath)
    val result = PlatformProcessSpawner.execute(
      "otool", listOf(
        "-L",
        path.fullPathString
      )
    )

    if (result.exitCode != 0) {
      return
    }
    val lines = result.stdoutAsString.lines()
    val linkedLibraryLines = lines.filter { it.startsWith("\t") }.map { it.trim() }
    val linkedFiles = linkedLibraryLines.asSequence().map { it.split("(").first().trim() }.distinct().map {
      PlatformPath(it)
    }.sortedBy { it.fullPathString }.toList()

    artifact.metadata.setList(
      this,
      MetadataKeys.DynamicLinkerLinkedFiles,
      FsPathSerializer,
      linkedFiles)

    val linkedFrameworks = linkedFiles.asSequence().filter { it.fullPathString.contains(".framework/") }
      .map { it.fullPathString.split(".framework/").first() + ".framework" }
      .map { PlatformPath(it) }
      .distinct()
      .sortedBy { it.fullPathString }.toList()

    artifact.metadata.setList(
      this,
      MetadataKeys.DynamicLinkerLinkedFrameworks,
      FsPathSerializer,
      linkedFrameworks
    )
  }
}
