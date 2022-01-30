package io.kexec.syscan.metadata

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class MetadataStoreSerializer : KSerializer<MetadataStore> {
  override fun deserialize(decoder: Decoder): MetadataStore {
    throw NotImplementedError()
  }

  override val descriptor: SerialDescriptor
    get() = EncodedMetadataStore.serializer().descriptor

  override fun serialize(encoder: Encoder, value: MetadataStore) {
    val properties = mutableMapOf<String, EncodedMetadataStore.EncodedMetadataProperty>()
    for ((key, entry) in value.metadata) {
      val property = EncodedMetadataStore.EncodedMetadataProperty(
        entry.source.metadataSourceKey,
        entry.encode()
      )
      properties["${key.namespace}:${key.id}"] = property
    }
    EncodedMetadataStore.serializer().serialize(encoder, EncodedMetadataStore(properties))
  }
}