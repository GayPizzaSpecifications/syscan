package io.kexec.syscan.concurrent

interface TaskPool {
  fun submit(task: () -> Unit)
  fun waitAndStop()
}
