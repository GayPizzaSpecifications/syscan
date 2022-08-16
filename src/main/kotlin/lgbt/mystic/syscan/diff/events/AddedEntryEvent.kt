package lgbt.mystic.syscan.diff.events

import lgbt.mystic.syscan.metadata.find
import lgbt.mystic.syscan.metadata.EncodedMetadataStore

class AddedEntryEvent(id: String) : DiffEvent(id) {
  override fun describe(
    oldReportEntries: List<EncodedMetadataStore>,
    newReportEntries: List<EncodedMetadataStore>
  ): String = "entry added $id ${newReportEntries.find(id).encodeToJson()}"
}
