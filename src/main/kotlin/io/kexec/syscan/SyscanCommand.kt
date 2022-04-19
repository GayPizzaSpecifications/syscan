package io.kexec.syscan

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import io.kexec.syscan.concurrent.SyncTaskPool

class SyscanCommand : CliktCommand(help = "System Scan Tool", name = "syscan", invokeWithoutSubcommand = true) {
  private val concurrency by option("--concurrency", "-c", help = "Task Concurrency").int().default(4)
  private val isSingleThreaded by option("--synchronous", "-s").flag()

  override fun run() {
    PlatformInit()
    currentContext.findOrSetObject {
      if (isSingleThreaded) {
        SyncTaskPool
      } else {
        PlatformTaskPool(concurrency)
      }
    }
  }
}
