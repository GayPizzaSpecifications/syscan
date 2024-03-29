package lgbt.mystic.syscan.diff

import lgbt.mystic.syscan.diff.events.*
import lgbt.mystic.syscan.metadata.EncodedMetadataStore

class ReportDiffer(val oldReportEntries: List<EncodedMetadataStore>, val newReportEntries: List<EncodedMetadataStore>) {
  fun diff(emit: (DiffEvent) -> Unit) {
    val oldReportIds = oldReportEntries.map { it.id }
    val newReportIds = newReportEntries.map { it.id }

    val removedEntryList = oldReportIds.filter { !newReportIds.contains(it) }
    val addedEntryList = newReportIds.filter { !oldReportIds.contains(it) }

    for (removedItemId in removedEntryList) {
      emit(RemovedEntryEvent(removedItemId))
    }

    for (addedItemId in addedEntryList) {
      emit(AddedEntryEvent(addedItemId))
    }

    val sharedEntryList = newReportIds.toMutableSet()
    sharedEntryList.removeIf { addedEntryList.contains(it) }

    for (sharedItemId in sharedEntryList) {
      val oldItem = oldReportEntries.first { it.id == sharedItemId }
      val newItem = newReportEntries.first { it.id == sharedItemId }

      val oldPropertyKeys = oldItem.properties.keys
      val newPropertyKeys = newItem.properties.keys

      val removedPropertyKeys = oldPropertyKeys.filter { !newPropertyKeys.contains(it) }
      val addedPropertyKeys = newPropertyKeys.filter { !oldPropertyKeys.contains(it) }

      for (removedPropertyKey in removedPropertyKeys) {
        emit(RemovedPropertyEvent(sharedItemId, removedPropertyKey))
      }

      for (addedPropertyKey in addedPropertyKeys) {
        emit(AddedPropertyEvent(sharedItemId, addedPropertyKey))
      }

      val sharedPropertyKeys = newPropertyKeys.toMutableSet()
      sharedPropertyKeys.removeIf { addedPropertyKeys.contains(it) }

      for (sharedPropertyKey in sharedPropertyKeys) {
        val oldValue = oldItem.properties[sharedPropertyKey]!!.value
        val newValue = newItem.properties[sharedPropertyKey]!!.value

        if (oldValue != newValue) {
          emit(ChangedPropertyEvent(sharedItemId, sharedPropertyKey))
        }
      }
    }
  }

  fun describe(event: DiffEvent): String = event.describe(oldReportEntries, newReportEntries)
}
