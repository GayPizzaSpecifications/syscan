package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.steps.*

object AnalysisSteps {
  val all = listOf(
    FileInfoStep,
    FileHashStep,
    UniversalBinaryStep,
    DynamicLinkerStep,
    MagicFileStep,
    IpswAnalysisStep
  )
}
