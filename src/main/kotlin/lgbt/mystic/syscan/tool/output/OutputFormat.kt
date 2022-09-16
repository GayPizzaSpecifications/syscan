package lgbt.mystic.syscan.tool.output

import lgbt.mystic.syscan.artifact.Artifact
import java.io.PrintStream

interface OutputFormat {
  fun write(out: PrintStream, artifact: Artifact)
}
