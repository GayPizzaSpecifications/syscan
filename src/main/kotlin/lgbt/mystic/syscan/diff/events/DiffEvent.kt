package lgbt.mystic.syscan.diff.events

import lgbt.mystic.syscan.metadata.EncodedMetadataStore

abstract class DiffEvent(val id: String) {
  abstract fun describe(
    oldReportEntries: List<EncodedMetadataStore>,
    newReportEntries: List<EncodedMetadataStore>
  ): String
}
