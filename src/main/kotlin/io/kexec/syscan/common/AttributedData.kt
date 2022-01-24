package io.kexec.syscan.common

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

@Serializable(with = AttributedDataSerializer::class)
class AttributedData(val typeKey: TypeKey, val attributor: Attributor) {
  internal val data = mutableMapOf<AttributionKey, JsonElement>()

  fun set(key: AttributionKey, value: JsonElement) {
    data[key] = value
  }

  fun set(key: AttributionKey, value: String) = set(key, JsonPrimitive(value))
  fun set(key: AttributionKey, value: Number) = set(key, JsonPrimitive(value))
  fun set(key: AttributionKey, value: Boolean) = set(key, JsonPrimitive(value))
  fun <T> set(key: AttributionKey, serializer: SerializationStrategy<T>, value: T) =
    set(key, Json.encodeToJsonElement(serializer, value))

  companion object {
    fun combine(data: List<AttributedData>): AttributedData {
      val result = AttributedData(data.first().typeKey, CompositeAttributor(data.map { it.attributor }))
      for (item in data) {
        result.data.putAll(item.data)
      }
      return result
    }

    fun combineWithNamespace(data: List<AttributedData>): AttributedData {
      val result = AttributedData(data.first().typeKey, CompositeAttributor(data.map { it.attributor }))
      for (item in data) {
        item.data.putAll(item.data.mapKeys { AttributionKey("${item.attributor.uniqueAttributorKey()}:${it.key}") })
      }
      return result
    }
  }
}
