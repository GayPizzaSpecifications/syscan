package lgbt.mystic.syscan.process.command

import lgbt.mystic.syscan.io.FsPath

class RawArgument(val argument: String): ExecutionParameter() {
  override fun toCommandArgument(workingDirectoryPath: FsPath): String = argument
}
