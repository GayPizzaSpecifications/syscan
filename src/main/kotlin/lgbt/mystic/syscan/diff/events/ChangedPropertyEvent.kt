package lgbt.mystic.syscan.diff.events

import lgbt.mystic.syscan.metadata.find
import lgbt.mystic.syscan.metadata.EncodedMetadataStore

class ChangedPropertyEvent(id: String, val key: String) : DiffEvent(id) {
  override fun describe(
    oldReportEntries: List<EncodedMetadataStore>,
    newReportEntries: List<EncodedMetadataStore>
  ): String = "property changed $key ${oldReportEntries.find(id, key)} to ${newReportEntries.find(id, key)}"
}
