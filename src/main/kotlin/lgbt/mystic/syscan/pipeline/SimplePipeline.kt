package lgbt.mystic.syscan.pipeline

class SimplePipeline<T> : Pipeline<T> {
  private val handlers = mutableListOf<PipelineHandler<T>>()

  override fun emit(item: T) {
    for (handler in handlers) {
      handler.handle(item)
    }
  }

  override fun addHandler(handler: PipelineHandler<T>) {
    handlers.add(handler)
  }

  override fun removeHandler(handler: PipelineHandler<T>) {
    handlers.removeAll { it == handler }
  }
}
