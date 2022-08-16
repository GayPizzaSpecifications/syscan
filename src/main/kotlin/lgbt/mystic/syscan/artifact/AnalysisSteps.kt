package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.steps.*

object AnalysisSteps {
  val systems = listOf(
    SystemInfoStep
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
