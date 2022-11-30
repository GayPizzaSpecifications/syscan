package lgbt.mystic.syscan.process.command

import lgbt.mystic.syscan.io.FsPath

class RelativeDirectoryPath(val path: FsPath): ExecutionParameter() {
  override fun toCommandArgument(workingDirectoryPath: FsPath): String {
    return path.relativeTo(workingDirectoryPath).fullPathString
  }
}
