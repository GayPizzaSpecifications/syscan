package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.io.FsPath
import lgbt.mystic.syscan.io.delete
import lgbt.mystic.syscan.metadata.CommonMetadataKeys
import lgbt.mystic.syscan.metadata.MetadataSource

abstract class ExtractableArtifact(id: String, val virtualFilePath: FsPath) : Artifact(id) {
  protected abstract fun extractToFile(): FsPath

  fun prepare(source: MetadataSource) {
    val temporaryFile = extractToFile()
    metadata.set(source, CommonMetadataKeys.ReadableFilePath, temporaryFile)
    metadata.set(source, CommonMetadataKeys.VirtualFilePath, virtualFilePath)
  }

  override fun cleanup() {
    super.cleanup()
    val path = metadata.get(CommonMetadataKeys.ReadableFilePath)
    path?.delete()
  }
}
