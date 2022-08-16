package io.kexec.syscan.pipeline

fun interface PipelineHandler<T> {
  fun handle(value: T)
}
