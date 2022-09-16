package lgbt.mystic.syscan.analysis.requirements

enum class OperatingSystem(val check: (String) -> Boolean) {
  Windows({ name -> name.contains("win") }),
  MacOS({ name -> name.contains("mac") }),
  Linux({ name -> name.contains("linux") }),
  Unknown({ true });

  companion object {
    val current: OperatingSystem by lazy {
      OperatingSystem.values().first { it.check(osNameLower) }
    }

    private val osNameLower: String by lazy {
      System.getProperty("os.name")!!.lowercase()
    }
  }
}
