package lgbt.mystic.syscan.dyld

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class IpswSharedCacheList(
  val magic: String,
  val uuid: String,
  val platform: String,
  @SerialName("max_slide")
  val maxSlide: Long? = null,
  val mappings: Map<String, List<IpswSharedCacheMapping>>,
  val dylibs: List<IpswSharedCacheDylib>
)

@Serializable
data class IpswSharedCacheMapping(
  val name: String,
  val address: Long,
  val size: Long,
  @SerialName("max_prot")
  val maxProtection: Int,
  @SerialName("init_prot")
  val initProtection: Int,
  @SerialName("SlideInfo")
  val slideInfo: JsonElement,
  @SerialName("Pages")
  val pages: JsonElement
)

@Serializable
data class IpswSharedCacheDylib(
  val index: Int,
  val name: String,
  val version: String,
  val uuid: String,
  @SerialName("load_address")
  val loadAddress: Long
)
