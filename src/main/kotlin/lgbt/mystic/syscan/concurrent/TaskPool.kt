package lgbt.mystic.syscan.concurrent

interface TaskPool {
  fun submit(task: () -> Unit)
  fun closeAndAwait()
}
