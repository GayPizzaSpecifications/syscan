package lgbt.mystic.syscan.concurrent

object DirectTaskPool : TaskPool {
  override fun submit(task: () -> Unit) {
    task()
  }

  override fun await() {}
  override fun close() {}
}
