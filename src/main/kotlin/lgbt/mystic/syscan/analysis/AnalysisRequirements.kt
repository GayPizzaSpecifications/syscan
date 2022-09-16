package lgbt.mystic.syscan.analysis

import lgbt.mystic.syscan.analysis.requirements.AnalysisRequirement
import lgbt.mystic.syscan.analysis.requirements.ExecutableRequirement
import lgbt.mystic.syscan.analysis.requirements.OperatingSystem
import lgbt.mystic.syscan.analysis.requirements.OperatingSystemRequirement

class AnalysisRequirements {
  private val requirements = mutableListOf<AnalysisRequirement>()

  fun os(os: OperatingSystem): Unit = require(OperatingSystemRequirement(os))
  fun executable(name: String): Unit = require(ExecutableRequirement(name))

  fun require(requirement: AnalysisRequirement) {
    requirements.add(requirement)
  }

  fun satisfied(): Boolean = requirements.all { it.satisfied() }
}
