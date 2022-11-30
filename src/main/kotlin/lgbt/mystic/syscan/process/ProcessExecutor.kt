package lgbt.mystic.syscan.process

import lgbt.mystic.syscan.PlatformCurrentWorkingDirectory
import lgbt.mystic.syscan.io.FsPath
import lgbt.mystic.syscan.process.command.ExecutionParameter

interface ProcessExecutor {
  fun execute(job: ExecutionJob): ProcessResult

  fun execute(command: List<ExecutionParameter>, workingDirectoryPath: FsPath = PlatformCurrentWorkingDirectory(), environment: Map<String, String> = mapOf()) =
    execute(ExecutionJob(command, workingDirectoryPath, environment))
}
