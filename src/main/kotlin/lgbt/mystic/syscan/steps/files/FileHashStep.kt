package lgbt.mystic.syscan.steps.files

import lgbt.mystic.syscan.PlatformHash
import lgbt.mystic.syscan.artifact.AnalysisContext
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.io.readBytesChunked
import lgbt.mystic.syscan.metadata.MetadataKeys
import lgbt.mystic.syscan.metadata.MetadataWants
import lgbt.mystic.syscan.metadata.keys.FileMetadataKeys

object FileHashStep : FileAnalysisStep {
  override val wants: MetadataWants = listOf(FileMetadataKeys.ReadableFilePath.want())
  override val provides: MetadataKeys = listOf(
    FileMetadataKeys.HashMd5,
    FileMetadataKeys.HashSha1,
    FileMetadataKeys.HashSha256,
    FileMetadataKeys.HashSha512
  )

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    val path = artifact.metadata.require(FileMetadataKeys.ReadableFilePath)

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

    artifact.metadata.set(this, FileMetadataKeys.HashMd5, md5Digest.toHexString())
    artifact.metadata.set(this, FileMetadataKeys.HashSha1, sha1Digest.toHexString())
    artifact.metadata.set(this, FileMetadataKeys.HashSha256, sha256Digest.toHexString())
    artifact.metadata.set(this, FileMetadataKeys.HashSha512, sha512Digest.toHexString())
  }
}
