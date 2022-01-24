package io.kexec.syscan

import com.github.ajalt.clikt.core.subcommands
import io.kexec.syscan.commands.ArtifactDiscoveryCommand

fun main(args: Array<String>) = SyscanCommand().subcommands(
  ArtifactDiscoveryCommand()
).main(args)
