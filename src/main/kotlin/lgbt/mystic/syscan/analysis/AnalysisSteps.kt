package lgbt.mystic.syscan.analysis

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
    MacDynamicLinkerStep,
    MagicFileStep,
    IpswAnalysisStep
  )

  val all = systems + files
}
