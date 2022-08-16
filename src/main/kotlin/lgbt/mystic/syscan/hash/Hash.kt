package lgbt.mystic.syscan.hash

interface Hash {
  fun update(bytes: ByteArray)
  fun update(bytes: ByteArray, offset: Int, size: Int)
  fun digest(): HashResult
}
