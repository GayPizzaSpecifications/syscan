package io.kexec.syscan.metadata

class MetadataSourcePlanner<T : MetadataSource>(private val sources: List<T>) {
  fun plan(): List<T> {
    val whatProvides = mutableMapOf<MetadataKey<*>, T>()
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
        if (whatProvides[requirement] == null) {
          throw Exception(
            "Source $source requires key $requirement but no provider was found"
          )
        }
      }
    }

    val extrapolatedKeyDependencies = mutableMapOf<MetadataKey<*>, List<MetadataKey<*>>>()
    for ((key, source) in whatProvides) {
      val requires = source.wants
      extrapolatedKeyDependencies[key] = requires
    }

    val stack = mutableListOf<MetadataKey<*>>()
    val visited = mutableSetOf<MetadataKey<*>>()

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
    return sortedSources
  }

  private fun resolve(
    key: MetadataKey<*>,
    stack: MutableList<MetadataKey<*>>,
    resolving: MutableSet<MetadataKey<*>>,
    visited: MutableSet<MetadataKey<*>>,
    dependencies: Map<MetadataKey<*>, List<MetadataKey<*>>>
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
