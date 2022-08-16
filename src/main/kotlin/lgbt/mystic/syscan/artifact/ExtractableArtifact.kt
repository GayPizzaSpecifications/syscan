package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.io.FsPath
import lgbt.mystic.syscan.io.delete
import lgbt.mystic.syscan.metadata.keys.FileMetadataKeys
import lgbt.mystic.syscan.metadata.MetadataSource

abstract class ExtractableArtifact(id: String, val virtualFilePath: FsPath) : Artifact(ArtifactKinds.File, id) {
  protected abstract fun extractToFile(): FsPath

  fun prepare(source: MetadataSource) {
    val temporaryFile = extractToFile()
    metadata.set(source, FileMetadataKeys.ReadableFilePath, temporaryFile)
    metadata.set(source, FileMetadataKeys.VirtualFilePath, virtualFilePath)
  }

  override fun cleanup() {
    super.cleanup()
    val path = metadata.get(FileMetadataKeys.ReadableFilePath)
    path?.delete()
  }
}
