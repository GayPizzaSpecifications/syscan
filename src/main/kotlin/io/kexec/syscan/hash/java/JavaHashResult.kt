package io.kexec.syscan.hash.java

import io.kexec.syscan.hash.HashResult
import java.security.MessageDigest

class JavaHashResult(val digest: MessageDigest): HashResult {
  override val bytes: ByteArray = digest.digest()
  override fun toHexString(): String = bytes.joinToString("") { b -> "%02x".format(b) }

  override fun equals(other: Any?): Boolean {
    if (other !is HashResult) return false
    return bytes.contentEquals(other.bytes)
  }

  override fun hashCode(): Int {
    return bytes.contentHashCode()
  }
}
