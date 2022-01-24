package io.kexec.syscan.common

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class EncodedAttributedData(
  val type: String,
  val attributor: String,
  val data: Map<String, JsonElement>
)
