package lgbt.mystic.syscan.process.command.analysis

import lgbt.mystic.syscan.io.FsPath

class ExecutionAnalysis(
  val requiredFilePaths: List<FsPath>,
  val requiredDirectoryPaths: List<FsPath>,
  val requiredEntityPaths: List<FsPath>,
  val requiredCommandNames: List<String>,
  val requiredSubCommandPatterns: List<List<String>>
)
