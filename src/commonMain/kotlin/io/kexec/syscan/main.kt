package io.kexec.syscan

import com.github.ajalt.clikt.core.subcommands
import io.kexec.syscan.commands.AnalyzeFiles

fun main(args: Array<String>) = SyscanCommand().subcommands(
  AnalyzeFiles()
).main(args)
