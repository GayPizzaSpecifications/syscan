package io.kexec.syscan.common

interface MetadataSource {
  val metadataSourceKey: String
    get() = this::class.simpleName!!

  val wants: List<MetadataKey<*>>
  val provides: List<MetadataKey<*>>
}
