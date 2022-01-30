package io.kexec.syscan.common

import kotlinx.serialization.json.JsonElement

data class MetadataEntry<T>(
  val source: MetadataSource,
  val value: T,
  val converter: (T) -> JsonElement
) {
  fun encode(): JsonElement = converter(value)
}
