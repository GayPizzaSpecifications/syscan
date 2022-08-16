package lgbt.mystic.syscan.metadata

fun <S: MetadataSource, I: HasMetadata> I.hasMetadataWants(source: S): Boolean =
  source.wants.all { metadata.has(it.key) }
