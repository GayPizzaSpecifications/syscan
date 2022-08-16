package lgbt.mystic.syscan.metadata

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

@Serializable
data class EncodedMetadataStore(
  val kind: String,
  val id: String,
  val properties: Map<String, EncodedMetadataProperty>
) {
  @Serializable
  data class EncodedMetadataProperty(
    val source: String,
    val value: JsonElement
  )

  fun encodeToJson(): String = Json.encodeToString(serializer(), this)
}
