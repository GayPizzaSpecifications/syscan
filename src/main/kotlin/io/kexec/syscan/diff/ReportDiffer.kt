package io.kexec.syscan.diff

import io.kexec.syscan.metadata.EncodedMetadataStore
import kotlinx.serialization.json.Json

class ReportDiffer(val oldReportEntries: List<EncodedMetadataStore>, val newReportEntries: List<EncodedMetadataStore>) {
  fun diff(block: (DiffEvent) -> Unit) {
    val oldReportIds = oldReportEntries.map { it.id }
    val newReportIds = newReportEntries.map { it.id }

    val removedEntryList = oldReportIds.filter { !newReportIds.contains(it) }
    val addedEntryList = newReportIds.filter { !oldReportIds.contains(it) }

    for (removedItemId in removedEntryList) {
      block(RemovedEntryEvent(removedItemId))
    }

    for (addedItemId in addedEntryList) {
      block(AddedEntryEvent(addedItemId))
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
        block(RemovedPropertyEvent(sharedItemId, removedPropertyKey))
      }

      for (addedPropertyKey in addedPropertyKeys) {
        block(AddedPropertyEvent(sharedItemId, addedPropertyKey))
      }

      val sharedPropertyKeys = newPropertyKeys.toMutableSet()
      sharedPropertyKeys.removeIf { addedPropertyKeys.contains(it) }

      for (sharedPropertyKey in sharedPropertyKeys) {
        val oldValue = oldItem.properties[sharedPropertyKey]!!.value
        val newValue = newItem.properties[sharedPropertyKey]!!.value

        if (oldValue != newValue) {
          block(ChangedPropertyEvent(sharedItemId, sharedPropertyKey))
        }
      }
    }
  }

  fun describe(event: DiffEvent): String = event.describe(oldReportEntries, newReportEntries)

  abstract class DiffEvent(val id: String) {
    abstract fun describe(oldReportEntries: List<EncodedMetadataStore>, newReportEntries: List<EncodedMetadataStore>): String
  }

  class RemovedEntryEvent(id: String) : DiffEvent(id) {
    override fun describe(
      oldReportEntries: List<EncodedMetadataStore>,
      newReportEntries: List<EncodedMetadataStore>
    ): String = "entry removed $id"
  }

  class AddedEntryEvent(id: String) : DiffEvent(id) {
    override fun describe(
      oldReportEntries: List<EncodedMetadataStore>,
      newReportEntries: List<EncodedMetadataStore>
    ): String = "entry added $id ${Json.encodeToString(EncodedMetadataStore.serializer(), newReportEntries.first { it.id == id })}"
  }

  class AddedPropertyEvent(id: String, val key: String) : DiffEvent(id) {
    override fun describe(
      oldReportEntries: List<EncodedMetadataStore>,
      newReportEntries: List<EncodedMetadataStore>
    ): String = "property added $id ${newReportEntries.first { it.id == id }.properties[key]!!.value}"
  }

  class RemovedPropertyEvent(id: String, val key: String) : DiffEvent(id) {
    override fun describe(
      oldReportEntries: List<EncodedMetadataStore>,
      newReportEntries: List<EncodedMetadataStore>
    ): String = "property removed $key ${oldReportEntries.first { it.id == id }.properties[key]!!.value}"
  }

  class ChangedPropertyEvent(id: String, val key: String) : DiffEvent(id) {
    override fun describe(
      oldReportEntries: List<EncodedMetadataStore>,
      newReportEntries: List<EncodedMetadataStore>
    ): String = "property changed $key ${oldReportEntries.first { it.id == id }.properties[key]!!.value} to ${newReportEntries.first { it.id == id }.properties[key]!!.value}"
  }
}
