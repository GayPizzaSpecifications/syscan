package lgbt.mystic.syscan.metadata

interface MetadataSource {
  val metadataSourceKey: String
    get() = this::class.simpleName!!

  val wants: MetadataWants
  val provides: MetadataKeys
}
