package lgbt.mystic.syscan.steps.files

import lgbt.mystic.syscan.PlatformPath
import lgbt.mystic.syscan.PlatformProcessSpawner
import lgbt.mystic.syscan.analysis.AnalysisContext
import lgbt.mystic.syscan.analysis.AnalysisRequirements
import lgbt.mystic.syscan.analysis.requirements.OperatingSystem
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.io.FsPathSerializer
import lgbt.mystic.syscan.metadata.MetadataKeys
import lgbt.mystic.syscan.metadata.MetadataWants
import lgbt.mystic.syscan.metadata.keys.FileMetadataKeys
import lgbt.mystic.syscan.process.command.CommandName
import lgbt.mystic.syscan.process.command.RawArgument
import lgbt.mystic.syscan.process.command.RelativeFilePath

object MacDynamicLinkerStep : FileAnalysisStep {
  override val wants: MetadataWants = listOf(FileMetadataKeys.ReadableFilePath.want(), FileMetadataKeys.MimeType.want())
  override val provides: MetadataKeys = listOf(FileMetadataKeys.DynamicLinkerLinkedFiles)

  override fun required(requirements: AnalysisRequirements): Unit = requirements.run {
    os(OperatingSystem.MacOS)
    executable("otool")
  }

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    val mime = artifact.metadata.require(FileMetadataKeys.MimeType)
    if (mime != MachBinaryType) {
      return
    }

    val path = artifact.metadata.require(FileMetadataKeys.ReadableFilePath)

    val result = PlatformProcessSpawner.execute(listOf(
      CommandName("otool"),
      RawArgument("-L"),
      RelativeFilePath(path)
    ))

    if (result.exitCode != 0) {
      return
    }
    val lines = result.stdoutAsString.lines()
    val linkedLibraryLines = lines
      .filter { it.startsWith("\t") }
      .map { it.trim() }
    val linkedFiles = linkedLibraryLines.asSequence()
      .map { it.split("(").first().trim() }
      .distinct()
      .map { PlatformPath(it) }
      .sorted()
      .toList()

    artifact.metadata.setList(
      this,
      FileMetadataKeys.DynamicLinkerLinkedFiles,
      FsPathSerializer,
      linkedFiles
    )

    val linkedFrameworks = linkedFiles.asSequence()
      .filter { it.fullPathString.contains(".framework/") }
      .map { it.fullPathString.split(".framework/").first() + ".framework" }
      .map { PlatformPath(it) }
      .distinct()
      .sorted()
      .toList()

    artifact.metadata.setList(
      this,
      FileMetadataKeys.DynamicLinkerLinkedFrameworks,
      FsPathSerializer,
      linkedFrameworks
    )
  }

  private const val MachBinaryType = "application/x-mach-binary"
}
