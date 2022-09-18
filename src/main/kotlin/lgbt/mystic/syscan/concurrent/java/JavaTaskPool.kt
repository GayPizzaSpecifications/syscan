package lgbt.mystic.syscan.concurrent.java

import lgbt.mystic.syscan.concurrent.TaskPool
import java.util.concurrent.ScheduledThreadPoolExecutor

class JavaTaskPool(concurrency: Int) : TaskPool {
  private val pool = ScheduledThreadPoolExecutor(concurrency)

  override fun submit(task: () -> Unit) {
    pool.submit(task)
  }

  override fun await() {
    while (pool.activeCount != 0) {
      Thread.yield()
    }
  }

  override fun close() {
    pool.shutdown()
  }
}
