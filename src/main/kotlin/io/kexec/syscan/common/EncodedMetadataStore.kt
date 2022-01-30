package io.kexec.syscan.common

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class EncodedMetadataStore(
  val properties: Map<String, EncodedMetadataProperty>
) {
  @Serializable
  data class EncodedMetadataProperty(
    val source: String,
    val value: JsonElement
  )
}
