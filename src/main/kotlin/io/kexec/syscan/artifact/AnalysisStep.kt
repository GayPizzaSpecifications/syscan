package io.kexec.syscan.artifact

import io.kexec.syscan.metadata.MetadataSource

interface AnalysisStep : MetadataSource {
  fun analyze(context: AnalysisContext, artifact: Artifact)
}
