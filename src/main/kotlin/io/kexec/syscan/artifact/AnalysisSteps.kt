package io.kexec.syscan.artifact

import io.kexec.syscan.steps.*

object AnalysisSteps {
  val all = listOf(
    FsinfoStep,
    FileHashStep,
    UniversalBinaryStep,
    DynamicLinkerStep,
    MagicFileStep
  )
}
