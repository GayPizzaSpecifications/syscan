package lgbt.mystic.syscan.tool.output

import lgbt.mystic.syscan.artifact.Artifact
import java.io.PrintStream

object SimpleOutputFormat : OutputFormat {
  override fun write(out: PrintStream, artifact: Artifact) {
    val metadata = artifact.metadata
    out.println("${metadata.kind.id} ${metadata.id}")

    for ((namespace, properties) in metadata.asCategoryMap()) {
      out.println("  $namespace")
      for ((key, value) in properties) {
        out.println("    $key = ${value.encode()}")
      }
    }
  }
}
