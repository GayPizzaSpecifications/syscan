package lgbt.mystic.syscan.process.command

import lgbt.mystic.syscan.io.FsPath

class SubCommandName(val subcommand: String): ExecutionParameter() {
  override fun toCommandArgument(workingDirectoryPath: FsPath): String = subcommand
}
