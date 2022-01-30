package io.kexec.syscan.steps

import io.kexec.syscan.artifact.AnalysisStep
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.common.MetadataKey
import io.kexec.syscan.common.MetadataKeys
import io.kexec.syscan.io.readBytesChunked
import java.security.MessageDigest

class FileHashStep : AnalysisStep {
  override val wants: List<MetadataKey<*>> = listOf(MetadataKeys.FilePath)
  override val provides: List<MetadataKey<*>> = listOf(MetadataKeys.HashSha1)

  override fun analyze(artifact: Artifact) {
    val path = artifact.metadata.require(MetadataKeys.FilePath)
    val sha1 = MessageDigest.getInstance("SHA-1")
    val sha256 = MessageDigest.getInstance("SHA-256")

    path.readBytesChunked { bytes, size ->
      sha1.update(bytes, 0, size)
      sha256.update(bytes, 0, size)
    }

    val sha1Digest = sha1.digest()
    val sha256Digest = sha256.digest()

    artifact.metadata.set(this, MetadataKeys.HashSha1, sha1Digest.toHex())
    artifact.metadata.set(this, MetadataKeys.HashSha256, sha256Digest.toHex())
  }

  private fun ByteArray.toHex() = joinToString("") { b -> "%02x".format(b) }
}
