package io.kexec.syscan.common

interface Attributor {
  fun uniqueAttributorKey(): String = this::class.simpleName!!
}
