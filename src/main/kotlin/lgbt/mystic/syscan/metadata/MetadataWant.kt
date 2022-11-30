package lgbt.mystic.syscan.metadata

class MetadataWant<T>(val key: MetadataKey<T>, val type: MetadataWantType) {
  fun isSatisfiedWith(item: HasMetadata) = if (type == MetadataWantType.Required) {
    item.metadata.has(key)
  } else true

  override fun hashCode(): Int {
    var result = key.hashCode()
    result = 31 * result + type.hashCode()
    return result
  }

  override fun equals(other: Any?): Boolean {
    if (other !is MetadataWant<*>) {
      return false
    }

    return other.key == key && other.type == type
  }

  override fun toString(): String = "Want(${type.name}: ${key})"
}
