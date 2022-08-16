package lgbt.mystic.syscan.metadata

internal fun EncodedMetadataStores.find(id: String): EncodedMetadataStore =
  first { it.id == id }
internal fun EncodedMetadataStores.find(id: String, key: String): EncodedMetadataProperty =
  find(id).properties[key]!!
