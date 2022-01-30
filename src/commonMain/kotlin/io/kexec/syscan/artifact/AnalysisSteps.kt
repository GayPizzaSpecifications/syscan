package io.kexec.syscan.artifact

import io.kexec.syscan.steps.FileHashStep
import io.kexec.syscan.steps.FsinfoStep

object AnalysisSteps {
  val all = listOf(
    FsinfoStep,
    FileHashStep
  )
}
