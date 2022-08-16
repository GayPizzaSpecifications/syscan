package lgbt.mystic.syscan.tool

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import lgbt.mystic.syscan.diff.events.DiffEvent
import lgbt.mystic.syscan.diff.ReportDiffer
import lgbt.mystic.syscan.io.fsPath
import lgbt.mystic.syscan.io.readJsonLines
import lgbt.mystic.syscan.metadata.EncodedMetadataStore

class CompareReportTool : CliktCommand("Compare Report", name = "compare-report") {
  private val oldReportPath by argument("old-report-path").fsPath(mustExist = true, canBeDir = false)
  private val newReportPath by argument("new-report-path").fsPath(mustExist = true, canBeDir = false)

  override fun run() {
    val oldReportEntries = oldReportPath.readJsonLines(EncodedMetadataStore.serializer())
    val newReportEntries = newReportPath.readJsonLines(EncodedMetadataStore.serializer())
    val differ = ReportDiffer(oldReportEntries, newReportEntries)

    val events = mutableMapOf<String, MutableList<DiffEvent>>()
    differ.diff { event ->
      events.getOrPut(event.id) { mutableListOf() }.add(event)
    }

    for ((id, eventsForId) in events) {
      println(id)
      for (event in eventsForId) {
        println("  ${differ.describe(event)}")
      }
    }
  }
}
