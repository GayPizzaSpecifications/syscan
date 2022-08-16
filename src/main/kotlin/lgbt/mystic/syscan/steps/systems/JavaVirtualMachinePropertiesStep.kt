package lgbt.mystic.syscan.steps.systems

import lgbt.mystic.syscan.artifact.AnalysisContext
import lgbt.mystic.syscan.artifact.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.metadata.MetadataKey
import lgbt.mystic.syscan.metadata.MetadataKeys
import lgbt.mystic.syscan.metadata.MetadataWants
import lgbt.mystic.syscan.metadata.keys.SystemMetadataKeys
import lgbt.mystic.syscan.system.AnalysisSystem

object JavaVirtualMachinePropertiesStep : AnalysisStep {
  override val wants: MetadataWants = emptyList()
  override val provides: MetadataKeys = emptyList()

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    if (artifact !is AnalysisSystem) {
      return
    }

    setJavaPropertyMetadata(artifact, SystemMetadataKeys.OperatingSystemName, "os.name")
    setJavaPropertyMetadata(artifact, SystemMetadataKeys.OperatingSystemVersion, "os.version")
    setJavaPropertyMetadata(artifact, SystemMetadataKeys.OperatingSystemArchitecture, "os.arch")
  }

  private fun setJavaPropertyMetadata(artifact: Artifact, key: MetadataKey<String>, property: String) {
    val value = System.getProperty(property)
    if (value != null) {
      artifact.metadata.set(this, key, value)
    }
  }
}
