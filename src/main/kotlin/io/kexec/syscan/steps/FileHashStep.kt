package io.kexec.syscan.steps

import io.kexec.syscan.PlatformHash
import io.kexec.syscan.artifact.AnalysisStep
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.metadata.MetadataKey
import io.kexec.syscan.metadata.MetadataKeys
import io.kexec.syscan.io.readBytesChunked

object FileHashStep : AnalysisStep {
  override val wants: List<MetadataKey<*>> = listOf(MetadataKeys.FilePath)
  override val provides: List<MetadataKey<*>> = listOf(
    MetadataKeys.HashMd5,
    MetadataKeys.HashSha1,
    MetadataKeys.HashSha256,
    MetadataKeys.HashSha512
  )

  override fun analyze(artifact: Artifact) {
    val path = artifact.metadata.require(MetadataKeys.FilePath)
    val md5 = PlatformHash("MD5")
    val sha1 = PlatformHash("SHA-1")
    val sha256 = PlatformHash("SHA-256")
    val sha512 = PlatformHash("SHA-512")

    path.readBytesChunked { bytes, size ->
      md5.update(bytes, 0, size)
      sha1.update(bytes, 0, size)
      sha256.update(bytes, 0, size)
      sha512.update(bytes, 0, size)
    }

    val md5Digest = md5.digest()
    val sha1Digest = sha1.digest()
    val sha256Digest = sha256.digest()
    val sha512Digest = sha512.digest()

    artifact.metadata.set(this, MetadataKeys.HashMd5, md5Digest.toHexString())
    artifact.metadata.set(this, MetadataKeys.HashSha1, sha1Digest.toHexString())
    artifact.metadata.set(this, MetadataKeys.HashSha256, sha256Digest.toHexString())
    artifact.metadata.set(this, MetadataKeys.HashSha512, sha512Digest.toHexString())
  }
}
