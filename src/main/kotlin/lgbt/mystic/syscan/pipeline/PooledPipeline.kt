package lgbt.mystic.syscan.pipeline

import lgbt.mystic.syscan.concurrent.TaskPool

class PooledPipeline<T>(val pool: TaskPool): Pipeline<T>() {
  override fun emit(value: T) {
    pool.submit {
      super.emit(value)
    }
  }
}
