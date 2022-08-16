package lgbt.mystic.syscan.tool

import com.github.ajalt.clikt.core.subcommands

fun main(args: Array<String>) = SyscanTool().subcommands(
  AnalyzerTool(),
  CompareReportTool()
).main(args)
