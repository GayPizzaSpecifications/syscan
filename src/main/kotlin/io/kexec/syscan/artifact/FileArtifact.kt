package io.kexec.syscan.artifact

import io.kexec.syscan.io.FsPath

class FileArtifact(val path: FsPath) : Artifact(path.fullPathString) {
  override fun toString(): String = "FileArtifact(${path})"
}
