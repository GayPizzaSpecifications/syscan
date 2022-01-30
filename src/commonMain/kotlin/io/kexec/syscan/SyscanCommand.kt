package io.kexec.syscan

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int

class SyscanCommand : CliktCommand(help = "System Scan Tool", name = "syscan", invokeWithoutSubcommand = true) {
  private val concurrency by option("--threads", "-t", help = "Task Concurrency").int().default(4)

  override fun run() {
    currentContext.findOrSetObject { PlatformTaskPool(concurrency) }
  }
}
