package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.metadata.HasMetadata
import lgbt.mystic.syscan.metadata.MetadataStore

open class Artifact(id: String) : HasMetadata {
  override val metadata: MetadataStore = MetadataStore(id)

  open fun cleanup() {}
}
