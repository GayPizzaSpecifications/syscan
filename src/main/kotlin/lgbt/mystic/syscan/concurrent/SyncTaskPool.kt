package lgbt.mystic.syscan.concurrent

object SyncTaskPool : TaskPool {
  override fun submit(task: () -> Unit) {
    task()
  }

  override fun waitAndStop() {}
}
