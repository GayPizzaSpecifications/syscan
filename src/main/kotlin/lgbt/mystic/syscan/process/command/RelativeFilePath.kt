package lgbt.mystic.syscan.process.command

import lgbt.mystic.syscan.io.FsPath

class RelativeFilePath(val path: FsPath): ExecutionParameter() {
  override fun toCommandArgument(workingDirectoryPath: FsPath): String {
    return path.relativeTo(workingDirectoryPath).fullPathString
  }
}
