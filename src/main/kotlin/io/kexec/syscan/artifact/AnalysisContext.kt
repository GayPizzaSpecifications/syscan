package io.kexec.syscan.artifact

interface AnalysisContext {
  fun emit(artifact: Artifact)
}
