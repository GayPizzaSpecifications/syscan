package io.kexec.syscan.artifact

import io.kexec.syscan.metadata.HasMetadata
import io.kexec.syscan.metadata.MetadataStore

open class Artifact : HasMetadata {
  override val metadata: MetadataStore = MetadataStore()
}

