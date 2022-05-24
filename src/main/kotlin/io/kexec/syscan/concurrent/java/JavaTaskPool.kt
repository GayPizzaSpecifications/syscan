package io.kexec.syscan.concurrent.java

import io.kexec.syscan.concurrent.TaskPool
import java.util.concurrent.ScheduledThreadPoolExecutor

class JavaTaskPool(concurrency: Int) : TaskPool {
  private val pool = ScheduledThreadPoolExecutor(concurrency)

  override fun submit(task: () -> Unit) {
    pool.submit(task)
  }

  override fun waitAndStop() {
    while (pool.activeCount != 0) {
      Thread.yield()
    }
    pool.shutdown()
  }
}
