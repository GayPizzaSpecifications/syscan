package lgbt.mystic.syscan.metadata

fun <S: MetadataSource, I: HasMetadata> I.satisfiesMetadataWantsOf(source: S): Boolean =
  source.wants.all { it.isSatisfiedWith(this) }
