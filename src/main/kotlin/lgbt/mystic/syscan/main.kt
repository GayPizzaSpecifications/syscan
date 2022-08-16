package lgbt.mystic.syscan

import com.github.ajalt.clikt.core.subcommands
import lgbt.mystic.syscan.commands.SystemAnalyzer
import lgbt.mystic.syscan.commands.CompareReport

fun main(args: Array<String>) = SyscanCommand().subcommands(
  SystemAnalyzer(),
  CompareReport()
).main(args)
