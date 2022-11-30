package lgbt.mystic.syscan.process.command.analysis

import lgbt.mystic.syscan.io.FsPath
import lgbt.mystic.syscan.process.ExecutionJob
import lgbt.mystic.syscan.process.command.*

class ExecutionAnalyzer(job: ExecutionJob) {
  private val expanded = job.expandSubParameters()

  fun analyze(): ExecutionAnalysis = ExecutionAnalysis(
    findRequiredFilePaths(),
    findRequiredDirectoryPaths(),
    findRequiredEntityPaths(),
    findRequiredCommands(),
    findSubCommandPatterns()
  )

  fun findRequiredFilePaths(): List<FsPath> {
    return expanded.filterIsInstance<RelativeFilePath>().map { it.path }
  }

  fun findRequiredDirectoryPaths(): List<FsPath> {
    return expanded.filterIsInstance<RelativeDirectoryPath>().map { it.path }
  }

  fun findRequiredEntityPaths(): List<FsPath> {
    return expanded.filterIsInstance<RelativePath>().map { it.path }
  }

  fun findRequiredCommands(): List<String> {
    val commands = mutableListOf<String>()

    for (parameter in expanded) {
      if (parameter is CommandName) {
        commands.add(parameter.command)
      }
    }

    return commands
  }

  fun findSubCommandPatterns(): List<List<String>> {
    val patterns = mutableListOf<List<String>>()
    val seen = mutableListOf<String>()

    for (parameter in expanded) {
      if (parameter is CommandName) {
        val commandName = parameter.command
        seen.clear()
        seen.add(commandName)
      }

      if (parameter is SubCommandName) {
        val commandName = parameter.subcommand
        seen.add(commandName)
        patterns.add(seen.toMutableList())
      }
    }
    return patterns
  }
}
