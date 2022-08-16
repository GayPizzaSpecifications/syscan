package lgbt.mystic.syscan.hash.java

import lgbt.mystic.syscan.hash.Hash
import lgbt.mystic.syscan.hash.HashResult
import java.security.MessageDigest

class JavaHash(val digest: MessageDigest) : Hash {
  override fun update(bytes: ByteArray) = digest.update(bytes)
  override fun update(bytes: ByteArray, offset: Int, size: Int) = digest.update(bytes, offset, size)
  override fun digest(): HashResult {
    return JavaHashResult(digest)
  }
}
