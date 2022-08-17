package lgbt.mystic.syscan.constructs

import lgbt.mystic.syscan.PlatformTaskPool
import lgbt.mystic.syscan.pipeline.PooledPipeline
import lgbt.mystic.syscan.pipeline.SimplePipeline

object PipelineUsage {
  @JvmStatic
  fun main(args: Array<String>) {
    val pipeline = SimplePipeline<String>()
    pipeline.addHandler { message ->
      println(message)
    }

    val pool = PlatformTaskPool(2)
    val pooled = PooledPipeline(pipeline, pool)
    pooled.emit("Hello World")
    pool.waitAndStop()
  }
}
