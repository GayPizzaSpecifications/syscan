package io.kexec.syscan.metadata

import java.util.Objects

data class MetadataKey<T>(val namespace: String, val id: String, val encodable: Boolean = true) {
  fun want(type: MetadataWantType = MetadataWantType.Required): MetadataWant<T> = MetadataWant(this, type)

  override fun equals(other: Any?): Boolean {
    if (other !is MetadataKey<*>) {
      return false
    }

    return other.namespace == namespace && other.id == id
  }

  override fun hashCode(): Int = Objects.hash(namespace, id)
  override fun toString(): String = "${namespace}:${id}"
}
