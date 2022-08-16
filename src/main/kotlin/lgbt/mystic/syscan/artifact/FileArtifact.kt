package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.io.FsPath

class FileArtifact(val path: FsPath) : Artifact(ArtifactKinds.File, path.fullPathString) {
  override fun toString(): String = "FileArtifact(${path})"
}
