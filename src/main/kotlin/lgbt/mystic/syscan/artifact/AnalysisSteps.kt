package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.steps.files.*
import lgbt.mystic.syscan.steps.systems.JvmPropertiesStep

object AnalysisSteps {
  val systems = listOf(
    JvmPropertiesStep
  )

  val files = listOf(
    FileInfoStep,
    FileHashStep,
    UniversalBinaryStep,
    DynamicLinkerStep,
    MagicFileStep,
    IpswAnalysisStep
  )

  val all = systems + files
}
