package io.kexec.syscan.io

interface FsPath {
  val fullPathString: String
  val entityNameString: String

  val parent: FsPath?
  val operations: FsOperations

  fun resolve(part: String): FsPath
  fun relativeTo(path: FsPath): FsPath
}
