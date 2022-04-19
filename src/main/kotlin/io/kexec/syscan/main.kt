package io.kexec.syscan

import com.github.ajalt.clikt.core.subcommands
import io.kexec.syscan.commands.AnalyzeFiles
import io.kexec.syscan.commands.CompareReport

fun main(args: Array<String>) = SyscanCommand().subcommands(
  AnalyzeFiles(),
  CompareReport()
).main(args)
