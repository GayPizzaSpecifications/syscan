package lgbt.mystic.syscan.artifact

import lgbt.mystic.syscan.metadata.HasMetadata
import lgbt.mystic.syscan.metadata.MetadataStore

open class Artifact(kind: ArtifactKind, id: String) : HasMetadata {
  override val metadata: MetadataStore = MetadataStore(kind, id)

  open fun cleanup() {}
}
