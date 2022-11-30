package lgbt.mystic.syscan.process.command

import lgbt.mystic.syscan.PlatformPath
import lgbt.mystic.syscan.io.FsPath

abstract class ExecutionParameter {
  abstract fun toCommandArgument(workingDirectoryPath: FsPath): String

  open fun listSubParameters(): List<ExecutionParameter> = emptyList()

  override fun toString(): String {
    return "${this::class.simpleName}(${toCommandArgument(PlatformPath("."))})"
  }
}
