package lgbt.mystic.syscan.tool.output

import lgbt.mystic.syscan.artifact.Artifact
import java.io.PrintStream

object JsonOutputFormat : OutputFormat {
  override fun write(out: PrintStream, artifact: Artifact) {
    out.println(artifact.metadata.encodeToJson().toString())
  }
}
