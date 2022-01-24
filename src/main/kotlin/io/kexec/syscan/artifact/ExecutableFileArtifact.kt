package io.kexec.syscan.artifact

import io.kexec.syscan.common.AttributedData
import io.kexec.syscan.common.CommonAttributeKeys
import io.kexec.syscan.io.FsPath

class ExecutableFileArtifact(path: FsPath) : FileArtifact(path) {
  override fun describe(): AttributedData = super.describe().apply {
    set(CommonAttributeKeys.ExecutableFileMarker, true)
  }
}
