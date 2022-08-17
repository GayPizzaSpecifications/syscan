package lgbt.mystic.syscan.pipeline

interface Pipeline<T> {
  fun emit(item: T)

  fun addHandler(handler: PipelineHandler<T>)
  fun removeHandler(handler: PipelineHandler<T>)
}
