package io.kexec.syscan.artifact

import io.kexec.syscan.common.HasMetadata
import io.kexec.syscan.common.MetadataStore

open class Artifact : HasMetadata {
  override val metadata: MetadataStore = MetadataStore()
}

