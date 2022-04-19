package io.kexec.syscan.io

class BasicFsPath(override val operations: FsOperations, override val fullPathString: String) : FsPath {
  override val entityNameString: String
    get() = fullPathString.split("/").last()

  override val parent: FsPath?
    get() {
      return if (fullPathString != "/") {
        BasicFsPath(operations, fullPathString
          .split("/")
          .dropLast(1)
          .joinToString("/"))
      } else {
        null
      }
    }

  override fun resolve(part: String): FsPath = BasicFsPath(operations, "$fullPathString/$part")

  override fun relativeTo(path: FsPath): FsPath {
    TODO("Not Implemented")
  }
}
