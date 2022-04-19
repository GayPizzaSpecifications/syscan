package io.kexec.syscan.hash.java

import io.kexec.syscan.hash.Hash
import io.kexec.syscan.hash.HashResult
import java.security.MessageDigest

class JavaHash(val digest: MessageDigest) : Hash {
  override fun update(bytes: ByteArray) = digest.update(bytes)
  override fun update(bytes: ByteArray, offset: Int, size: Int) = digest.update(bytes, offset, size)
  override fun digest(): HashResult {
    return object : HashResult {
      override val bytes: ByteArray = digest.digest()
      override fun toHexString(): String = bytes.joinToString("") { b -> "%02x".format(b) }
    }
  }
}
