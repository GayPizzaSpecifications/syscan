package io.kexec.syscan.metadata

import io.kexec.syscan.PlatformConcurrentMap
import io.kexec.syscan.io.FsPath
import io.kexec.syscan.io.FsPathSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import java.time.Instant

@Serializable(with = MetadataStoreSerializer::class)
class MetadataStore(val id: String) {
  internal val metadata = PlatformConcurrentMap<MetadataKey<*>, MetadataEntry<*>>()

  fun <T> set(source: MetadataSource, key: MetadataKey<T>, value: T, converter: (T) -> JsonElement) {
    metadata[key] = MetadataEntry(source, value, converter)
  }

  fun set(source: MetadataSource, key: MetadataKey<String>, value: String) =
    set(source, key, value) { JsonPrimitive(value) }

  fun set(source: MetadataSource, key: MetadataKey<Number>, value: Number) =
    set(source, key, value) { JsonPrimitive(value) }

  fun set(source: MetadataSource, key: MetadataKey<Boolean>, value: Boolean) =
    set(source, key, value) { JsonPrimitive(value) }

  fun set(source: MetadataSource, key: MetadataKey<Instant>, value: Instant) =
    set(source, key, value) { JsonPrimitive(value.toString()) }

  fun set(source: MetadataSource, key: MetadataKey<List<String>>, value: List<String>) =
    set(source, key, ListSerializer(String.serializer()), value)

  fun set(source: MetadataSource, key: MetadataKey<FsPath>, value: FsPath) =
    set(source, key, FsPathSerializer, value)

  fun <T> setList(source: MetadataSource, key: MetadataKey<List<T>>, valueSerializer: KSerializer<T>, value: List<T>) =
    set(source, key, ListSerializer(valueSerializer), value)

  fun <T> set(source: MetadataSource, key: MetadataKey<T>, serializer: SerializationStrategy<T>, value: T) =
    set(source, key, value) { Json.encodeToJsonElement(serializer, value) }

  fun <T> get(key: MetadataKey<T>): T? {
    val entry = metadata[key] ?: return null
    @Suppress("UNCHECKED_CAST")
    return entry.value as T
  }

  fun <T> require(key: MetadataKey<T>): T {
    val entry = metadata[key]
      ?: throw Exception("Metadata key $key was not found and it is required.")
    @Suppress("UNCHECKED_CAST")
    return entry.value as T
  }

  fun <T> has(it: MetadataKey<T>) = metadata[it] != null

  fun encodeToJson(): JsonElement = Json.encodeToJsonElement(serializer(), this)
}
