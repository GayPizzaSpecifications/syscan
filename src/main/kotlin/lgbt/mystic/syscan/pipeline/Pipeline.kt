package lgbt.mystic.syscan.pipeline

open class Pipeline<T> {
  private val handlers = mutableListOf<PipelineHandler<T>>()

  fun addHandler(handler: PipelineHandler<T>) {
    handlers.add(handler)
  }

  fun removeHandler(handler: PipelineHandler<T>) {
    handlers.removeAll { it == handler }
  }

  open fun emit(value: T) {
    for (handler in handlers) {
      handler.handle(value)
    }
  }
}
