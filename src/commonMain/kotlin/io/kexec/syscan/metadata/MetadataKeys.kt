package io.kexec.syscan.metadata

import io.kexec.syscan.io.FsPath

object MetadataKeys {
  val FilePath = MetadataKey<FsPath>("filesystem", "path")
  val ExecutableFileMarker = MetadataKey<Boolean>("filesystem", "executable")
  val HashSha1 = MetadataKey<String>("hash", "sha1")
  val HashSha256 = MetadataKey<String>("hash", "sha256")
}
