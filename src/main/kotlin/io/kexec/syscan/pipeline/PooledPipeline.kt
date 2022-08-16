package io.kexec.syscan.pipeline

import io.kexec.syscan.concurrent.TaskPool

class PooledPipeline<T>(val pool: TaskPool): Pipeline<T>() {
  override fun emit(value: T) {
    pool.submit {
      super.emit(value)
    }
  }
}
