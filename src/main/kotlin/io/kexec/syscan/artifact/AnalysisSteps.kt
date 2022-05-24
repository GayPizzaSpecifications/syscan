package io.kexec.syscan.artifact

import io.kexec.syscan.steps.*

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
