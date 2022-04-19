package io.kexec.syscan.metadata

interface MetadataSource {
  val metadataSourceKey: String
    get() = this::class.simpleName!!

  val wants: List<MetadataKey<*>>
  val provides: List<MetadataKey<*>>
}
