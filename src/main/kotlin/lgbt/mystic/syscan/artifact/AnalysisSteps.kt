package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.steps.files.*
import lgbt.mystic.syscan.steps.systems.JavaVirtualMachinePropertiesStep

object AnalysisSteps {
  val systems = listOf(
    JavaVirtualMachinePropertiesStep
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
