package io.kexec.syscan.metadata

import io.kexec.syscan.io.FsPath
import java.time.Instant

object CommonMetadataKeys {
  val ReadableFilePath = MetadataKey<FsPath>("filesystem", "readable-path", encodable = false)
  val VirtualFilePath = MetadataKey<FsPath>("filesystem", "path")

  val ExecutableFileMarker = MetadataKey<Boolean>("filesystem", "executable")
  val MimeType = MetadataKey<String>("filesystem", "mime-type")
  val LastModified = MetadataKey<Instant>("filesystem", "last-modified")

  val HashMd5 = MetadataKey<String>("hash", "md5")
  val HashSha1 = MetadataKey<String>("hash", "sha1")
  val HashSha256 = MetadataKey<String>("hash", "sha256")
  val HashSha512 = MetadataKey<String>("hash", "sha512")

  val UniversalBinaryArchitectures = MetadataKey<List<String>>("universal-binary", "architectures")
  val DynamicLinkerLinkedFiles = MetadataKey<List<FsPath>>("dynamic-linker", "linked-files")
  val DynamicLinkerLinkedFrameworks = MetadataKey<List<FsPath>>("dynamic-linker", "linked-frameworks")
}
