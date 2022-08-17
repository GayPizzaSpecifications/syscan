package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.metadata.HasMetadata
import lgbt.mystic.syscan.metadata.MetadataKind
import lgbt.mystic.syscan.metadata.MetadataStore

open class Artifact(kind: MetadataKind, id: String) : HasMetadata {
  override val metadata: MetadataStore = MetadataStore(kind, id)

  open fun cleanup() {}
}
