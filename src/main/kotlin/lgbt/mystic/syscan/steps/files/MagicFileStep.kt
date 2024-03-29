package lgbt.mystic.syscan.steps.files

import lgbt.mystic.syscan.PlatformProcessSpawner
import lgbt.mystic.syscan.analysis.AnalysisContext
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.metadata.MetadataKeys
import lgbt.mystic.syscan.metadata.MetadataWants
import lgbt.mystic.syscan.metadata.keys.FileMetadataKeys
import lgbt.mystic.syscan.process.command.CommandName
import lgbt.mystic.syscan.process.command.RawArgument
import lgbt.mystic.syscan.process.command.RelativeFilePath

object MagicFileStep : FileAnalysisStep {
  override val wants: MetadataWants = listOf(FileMetadataKeys.ReadableFilePath.want())
  override val provides: MetadataKeys = listOf(FileMetadataKeys.MimeType)

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    val path = artifact.metadata.require(FileMetadataKeys.ReadableFilePath)
    val result = PlatformProcessSpawner.execute(listOf(
      CommandName("file"),
      RawArgument("--brief"),
      RawArgument("--mime-type"),
      RelativeFilePath(path)
    ), environment = mapOf("POSIXLY_CORRECT" to "1"))

    if (result.exitCode != 0) {
      return
    }

    val mime = result.stdoutAsString.lines().first().trim()
    artifact.metadata.set(this, FileMetadataKeys.MimeType, mime)
  }
}
