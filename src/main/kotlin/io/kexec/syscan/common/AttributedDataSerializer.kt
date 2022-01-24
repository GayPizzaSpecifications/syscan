package io.kexec.syscan.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class AttributedDataSerializer : KSerializer<AttributedData> {
  override fun deserialize(decoder: Decoder): AttributedData {
    throw NotImplementedError()
  }

  override val descriptor: SerialDescriptor
    get() = EncodedAttributedData.serializer().descriptor

  override fun serialize(encoder: Encoder, value: AttributedData) {
    val encoded = EncodedAttributedData(
      type = value.typeKey.id,
      attributor = value.attributor.uniqueAttributorKey(),
      data = value.data.mapKeys { it.key.id }
    )
    EncodedAttributedData.serializer().serialize(encoder, encoded)
  }
}
