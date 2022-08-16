package lgbt.mystic.syscan.tool

import com.github.ajalt.clikt.core.subcommands
import lgbt.mystic.syscan.tool.AnalyzerTool
import lgbt.mystic.syscan.tool.CompareReportTool
import lgbt.mystic.syscan.tool.SyscanTool

fun main(args: Array<String>) = SyscanTool().subcommands(
  AnalyzerTool(),
  CompareReportTool()
).main(args)
