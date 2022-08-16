package lgbt.mystic.syscan.diff.events

import lgbt.mystic.syscan.metadata.EncodedMetadataStore

class RemovedEntryEvent(id: String) : DiffEvent(id) {
  override fun describe(
    oldReportEntries: List<EncodedMetadataStore>,
    newReportEntries: List<EncodedMetadataStore>
  ): String = "entry removed $id"
}
