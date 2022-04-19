package io.kexec.syscan.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import io.kexec.syscan.diff.ReportDiffer
import io.kexec.syscan.io.fsPath
import io.kexec.syscan.io.readJsonLines
import io.kexec.syscan.metadata.EncodedMetadataStore

class CompareReport : CliktCommand("Compare Report", name = "compare-report") {
  private val oldReportPath by argument("old-report-path").fsPath(mustExist = true, canBeDir = false)
  private val newReportPath by argument("new-report-path").fsPath(mustExist = true, canBeDir = false)

  override fun run() {
    val oldReportEntries = oldReportPath.readJsonLines(EncodedMetadataStore.serializer())
    val newReportEntries = newReportPath.readJsonLines(EncodedMetadataStore.serializer())
    val differ = ReportDiffer(oldReportEntries, newReportEntries)

    val events = mutableMapOf<String, MutableList<ReportDiffer.DiffEvent>>()
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
