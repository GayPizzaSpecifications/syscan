package io.kexec.syscan.artifact

import io.kexec.syscan.common.AttributedData
import io.kexec.syscan.common.CommonAttributeKeys
import io.kexec.syscan.common.CommonTypeKeys
import io.kexec.syscan.io.FsPath

open class FileArtifact(val path: FsPath) : Artifact {
  override fun describe(): AttributedData = AttributedData(CommonTypeKeys.Artifact, this).apply {
    set(CommonAttributeKeys.FilePath, path.fullPathString)
  }
}
