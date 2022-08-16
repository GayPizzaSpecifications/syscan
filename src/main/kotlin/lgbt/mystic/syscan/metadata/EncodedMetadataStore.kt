package lgbt.mystic.syscan.metadata

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class EncodedMetadataStore(
  val kind: String,
  val id: String,
  val properties: Map<String, EncodedMetadataProperty>
) {
  fun encodeToJson(): String = Json.encodeToString(serializer(), this)
}
