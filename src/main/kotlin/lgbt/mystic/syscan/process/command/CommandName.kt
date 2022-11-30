package lgbt.mystic.syscan.process.command

import lgbt.mystic.syscan.io.FsPath

class CommandName(val command: String): ExecutionParameter() {
  override fun toCommandArgument(workingDirectoryPath: FsPath): String = command
}
