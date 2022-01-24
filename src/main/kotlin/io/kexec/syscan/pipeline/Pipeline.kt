package io.kexec.syscan.pipeline

import kotlin.reflect.KClass

class Pipeline<T: Any>(private val root: KClass<T>) {
  private val handlers = mutableMapOf<KClass<T>, MutableList<PipelineHandler<T>>>()

  fun addHandler(handler: PipelineHandler<T>) = addHandler(root, handler)
  fun addHandler(handler: (T) -> Unit) = addHandler(root, handler)

  fun <V: T> addHandler(type: KClass<V>, handler: PipelineHandler<V>) {
    @Suppress("UNCHECKED_CAST")
    handlers.computeIfAbsent(type as KClass<T>) { mutableListOf() }.add(handler as PipelineHandler<T>)
  }

  fun <V: T> addHandler(type: KClass<V>, handler: (V) -> Unit) {
    @Suppress("UNCHECKED_CAST")
    handlers.computeIfAbsent(type as KClass<T>) { mutableListOf() }.add(object : PipelineHandler<V> {
      override fun handle(value: V) {
        handler(value)
      }
    } as PipelineHandler<T>)
  }

  fun <V: T> pipe(value: V) {
    handlers.entries.filter {
      it.key.isInstance(value)
    }.forEach {
      it.value.forEach { handler ->
        if (handler.filter(value)) {
          handler.handle(value)
        }
      }
    }
  }
}
