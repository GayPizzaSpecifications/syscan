package lgbt.mystic.syscan.analysis.requirements

class OperatingSystemRequirement(val os: OperatingSystem) : AnalysisRequirement {
  override fun satisfied(): Boolean = OperatingSystem.current == os
}
