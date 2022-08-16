package lgbt.mystic.syscan.hash

interface HashResult {
  val bytes: ByteArray
  fun toHexString(): String
}
