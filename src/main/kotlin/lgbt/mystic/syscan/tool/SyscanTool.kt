package lgbt.mystic.syscan.tool

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import lgbt.mystic.syscan.PlatformInit
import lgbt.mystic.syscan.PlatformTaskPool
import lgbt.mystic.syscan.concurrent.LoopTaskPool
import lgbt.mystic.syscan.concurrent.DirectTaskPool

class SyscanTool : CliktCommand(help = "System Scan Tool", name = "syscan", invokeWithoutSubcommand = true) {
  private val concurrency by option("--concurrency", "-c", help = "Task Concurrency").int().default(4)
  private val isSingleThreaded by option("--synchronous", "-s").flag()
  private val isSingleLoopThreaded by option("--synchronous-loop").flag()

  override fun run() {
    PlatformInit()
    currentContext.findOrSetObject {
      if (isSingleLoopThreaded) {
        LoopTaskPool()
      } else if (isSingleThreaded) {
        DirectTaskPool
      } else {
        PlatformTaskPool(concurrency)
      }
    }
  }
}
