package io.kexec.syscan.pipeline

interface PipelineHandler<T> {
  fun filter(value: T): Boolean = true
  fun handle(value : T)
}
