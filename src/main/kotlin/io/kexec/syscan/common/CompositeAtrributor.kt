package io.kexec.syscan.common

class CompositeAttributor(private val attributors: List<Attributor>) : Attributor {
  override fun uniqueAttributorKey(): String = attributors
    .map { attributor ->  attributor.uniqueAttributorKey() }
    .sorted()
    .joinToString(":")
}
