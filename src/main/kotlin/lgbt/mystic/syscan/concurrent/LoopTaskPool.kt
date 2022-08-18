package lgbt.mystic.syscan.concurrent

class LoopTaskPool : TaskPool {
  private val tasks = mutableListOf<() -> Unit>()

  override fun submit(task: () -> Unit) {
    tasks.add(task)
  }

  fun flush() {
    while (tasks.isNotEmpty()) {
      val task = tasks.removeAt(0)
      task()
    }
  }

  override fun closeAndAwait() {
    flush()
  }
}
