package io.kexec.syscan.artifact

import io.kexec.syscan.common.MetadataSource

interface AnalysisStep : MetadataSource {
  fun analyze(artifact: Artifact)
}
