package io.kexec.syscan.hash

interface HashResult {
  val bytes: ByteArray
  fun toHexString(): String
}
