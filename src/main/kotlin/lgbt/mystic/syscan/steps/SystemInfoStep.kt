package lgbt.mystic.syscan.steps

import lgbt.mystic.syscan.artifact.AnalysisContext
import lgbt.mystic.syscan.artifact.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.metadata.CommonMetadataKeys
import lgbt.mystic.syscan.metadata.MetadataKey
import lgbt.mystic.syscan.metadata.MetadataKeys
import lgbt.mystic.syscan.metadata.MetadataWants
import lgbt.mystic.syscan.system.AnalysisSystem

object SystemInfoStep : AnalysisStep {
  override val wants: MetadataWants = emptyList()
  override val provides: MetadataKeys = emptyList()

  override fun analyze(context: AnalysisContext, artifact: Artifact) {
    if (artifact !is AnalysisSystem) {
      return
    }

    setJavaPropertyMetadata(artifact, CommonMetadataKeys.OperatingSystemName, "os.name")
  }

  fun setJavaPropertyMetadata(artifact: Artifact, key: MetadataKey<String>, property: String) {
    artifact.metadata.set(this, key, System.getProperty(property))
  }
}
