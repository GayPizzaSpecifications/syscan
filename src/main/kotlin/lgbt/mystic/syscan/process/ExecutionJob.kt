package lgbt.mystic.syscan.process

import lgbt.mystic.syscan.process.command.analysis.ExecutionAnalyzer
import lgbt.mystic.syscan.io.FsPath
import lgbt.mystic.syscan.process.command.ExecutionParameter
import lgbt.mystic.syscan.process.command.analysis.ExecutionAnalysis

class ExecutionJob(
  val command: List<ExecutionParameter>,
  val workingDirectoryPath: FsPath,
  val environment: Map<String, String>
) {
  fun expandCommandArguments(): List<String> = command.map { parameter ->
    parameter.toCommandArgument(workingDirectoryPath)
  }

  fun expandSubParameters(): List<ExecutionParameter> {
    val all = mutableListOf<ExecutionParameter>()
    for (parameter in command) {
      all.add(parameter)
      all.addAll(parameter.listSubParameters())
    }
    return all
  }

  fun analyze(): ExecutionAnalysis = ExecutionAnalyzer(this).analyze()
}
