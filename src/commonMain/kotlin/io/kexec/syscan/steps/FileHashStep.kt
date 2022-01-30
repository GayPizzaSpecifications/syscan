package io.kexec.syscan.steps

import io.kexec.syscan.PlatformHash
import io.kexec.syscan.artifact.AnalysisStep
import io.kexec.syscan.artifact.Artifact
import io.kexec.syscan.metadata.MetadataKey
import io.kexec.syscan.metadata.MetadataKeys
import io.kexec.syscan.io.readBytesChunked

object FileHashStep : AnalysisStep {
  override val wants: List<MetadataKey<*>> = listOf(MetadataKeys.FilePath)
  override val provides: List<MetadataKey<*>> = listOf(MetadataKeys.HashSha1)

  override fun analyze(artifact: Artifact) {
    val path = artifact.metadata.require(MetadataKeys.FilePath)
    val sha1 = PlatformHash("SHA-1")
    val sha256 = PlatformHash("SHA-256")

    path.readBytesChunked { bytes, size ->
      sha1.update(bytes, 0, size)
      sha256.update(bytes, 0, size)
    }

    val sha1Digest = sha1.digest()
    val sha256Digest = sha256.digest()

    artifact.metadata.set(this, MetadataKeys.HashSha1, sha1Digest.toHexString())
    artifact.metadata.set(this, MetadataKeys.HashSha256, sha256Digest.toHexString())
  }
}
