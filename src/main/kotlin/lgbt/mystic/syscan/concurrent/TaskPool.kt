package lgbt.mystic.syscan.concurrent

interface TaskPool {
  fun submit(task: () -> Unit)
  fun await()
  fun close()
}
