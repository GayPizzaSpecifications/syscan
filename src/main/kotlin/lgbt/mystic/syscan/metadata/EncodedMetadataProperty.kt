package lgbt.mystic.syscan.metadata

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class EncodedMetadataProperty(
  val source: String,
  val value: JsonElement
)
