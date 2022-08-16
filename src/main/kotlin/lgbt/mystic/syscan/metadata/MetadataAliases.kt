package lgbt.mystic.syscan.metadata

typealias AnyMetadataKey = MetadataKey<*>
typealias AnyMetadataWant = MetadataWant<*>
typealias MetadataKeys = List<AnyMetadataKey>
typealias MetadataWants = List<AnyMetadataWant>
typealias MetadataKeysOf<T> = List<MetadataKey<T>>
typealias MetadataWantsOf<T> = List<MetadataWant<T>>
