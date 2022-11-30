package lgbt.mystic.syscan.analysis.requirements

import lgbt.mystic.syscan.PlatformPath
import lgbt.mystic.syscan.PlatformPathSeparator
import lgbt.mystic.syscan.io.isExecutable

class ExecutableRequirement(val name: String) : AnalysisRequirement {
  override fun satisfied(): Boolean {
    val names = if (OperatingSystem.current == OperatingSystem.Windows) {
      listOf(name, "${name}.exe")
    } else {
      listOf(name)
    }

    val pathEnvValue = System.getenv("PATH") ?: return false
    val paths = pathEnvValue.split(PlatformPathSeparator()).map { PlatformPath(it) }
    for (path in paths) {
      val pathsToCheck = names.map { name -> path.resolve(name) }
      for (pathToCheck in pathsToCheck) {
        if (pathToCheck.isExecutable()) {
          return true
        }
      }
    }
    return false
  }
}
