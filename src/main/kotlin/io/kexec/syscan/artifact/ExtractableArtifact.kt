package io.kexec.syscan.artifact

import io.kexec.syscan.io.FsPath
import io.kexec.syscan.io.delete
import io.kexec.syscan.metadata.CommonMetadataKeys
import io.kexec.syscan.metadata.MetadataSource

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
