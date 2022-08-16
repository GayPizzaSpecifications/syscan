package lgbt.mystic.syscan.metadata.keys

import lgbt.mystic.syscan.metadata.MetadataKey

object SystemMetadataKeys {
  val OperatingSystemName = MetadataKey<String>("operating-system", "name")
  val OperatingSystemVersion = MetadataKey<String>("operating-system", "version")
  val OperatingSystemArchitecture = MetadataKey<String>("operating-system", "architecture")
}
