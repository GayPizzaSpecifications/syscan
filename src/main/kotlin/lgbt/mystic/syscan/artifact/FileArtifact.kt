package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.io.FsPath

class FileArtifact(val path: FsPath) : Artifact(path.fullPathString) {
  override fun toString(): String = "FileArtifact(${path})"
}
