package lgbt.mystic.syscan.hash

interface Hash {
  val name: String

  fun update(bytes: ByteArray)
  fun update(bytes: ByteArray, offset: Int, size: Int)
  fun digest(): HashResult
}
