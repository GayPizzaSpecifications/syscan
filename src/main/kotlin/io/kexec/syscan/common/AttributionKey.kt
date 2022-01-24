package io.kexec.syscan.common

class AttributionKey(val id: String) {
  override fun equals(other: Any?): Boolean {
    if (other == null) {
      return false
    }

    if (other !is AttributionKey) {
      return false
    }

    return other.id == id
  }

  override fun hashCode(): Int = id.hashCode()

  override fun toString(): String = "AttributionKey(${id})"
}
