package lgbt.mystic.syscan.pipeline

fun interface PipelineHandler<T> {
  fun handle(value: T)
}
