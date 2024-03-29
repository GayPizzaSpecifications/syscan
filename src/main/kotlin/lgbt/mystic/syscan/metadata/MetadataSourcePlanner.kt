package lgbt.mystic.syscan.metadata

class MetadataSourcePlanner<T : MetadataSource>(private val sources: List<T>) {
  fun plan(): List<T> {
    val whatProvides = mutableMapOf<AnyMetadataKey, T>()
    for (source in sources) {
      val sourceProvides = source.provides
      for (provide in sourceProvides) {
        if (whatProvides[provide] != null) {
          throw Exception(
            "Source $source declares it provides key $provide but" +
                " ${whatProvides[provide]} is already declared as the provider for $provide"
          )
        }

        whatProvides[provide] = source
      }
    }

    for (source in sources) {
      val requires = source.wants
      for (requirement in requires) {
        if (requirement.type ==  MetadataWantType.Required && whatProvides[requirement.key] == null) {
          throw Exception(
            "Source $source requires key $requirement but no provider was found"
          )
        }
      }
    }

    val extrapolatedKeyDependencies = mutableMapOf<AnyMetadataKey, List<AnyMetadataKey>>()
    for ((key, source) in whatProvides) {
      val requires = source.wants
      extrapolatedKeyDependencies[key] = requires.map { it.key }
    }

    val stack = mutableListOf<AnyMetadataKey>()
    val visited = mutableSetOf<AnyMetadataKey>()

    for (key in whatProvides.keys) {
      resolve(key, stack, mutableSetOf(), visited, extrapolatedKeyDependencies)
    }

    val sortedSourcesWithPossibleDupes = stack.map { whatProvides[it]!! }
    val sortedSources = mutableListOf<T>()
    for (source in sortedSourcesWithPossibleDupes) {
      if (!sortedSources.contains(source)) {
        sortedSources.add(source)
      }
    }

    for (source in sources) {
      if (!sortedSources.contains(source)) {
        sortedSources.add(source)
      }
    }

    return sortedSources
  }

  private fun resolve(
    key: AnyMetadataKey,
    stack: MutableList<AnyMetadataKey>,
    resolving: MutableSet<AnyMetadataKey>,
    visited: MutableSet<AnyMetadataKey>,
    dependencies: Map<AnyMetadataKey, List<AnyMetadataKey>>
  ) {
    val dependenciesOfKey = dependencies[key]
    if (dependenciesOfKey != null) {
      for (dependency in dependenciesOfKey) {
        if (resolving.contains(dependency)) {
          throw Exception("Circular dependency detected on key $dependency")
        }

        if (!visited.contains(dependency)) {
          resolving.add(dependency)
          resolve(dependency, stack, resolving, visited, dependencies)
          resolving.remove(dependency)
          visited.add(key)
        }
      }
      stack.add(key)
    }
  }
}
