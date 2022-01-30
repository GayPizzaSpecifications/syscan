package io.kexec.syscan.io

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

interface FsOperations {
  fun exists(path: FsPath): Boolean
  fun isDirectory(path: FsPath): Boolean
  fun isRegularFile(path: FsPath): Boolean
  fun isSymbolicLink(path: FsPath): Boolean
  fun isReadable(path: FsPath): Boolean
  fun isWritable(path: FsPath): Boolean
  fun isExecutable(path: FsPath): Boolean

  fun list(path: FsPath): Sequence<FsPath>

  fun walk(path: FsPath): Sequence<FsPath>
  fun visit(path: FsPath, visitor: FsPathVisitor)

  fun readString(path: FsPath): String
  fun readAllBytes(path: FsPath): ByteArray
  fun readBytesChunked(path: FsPath, block: (ByteArray, Int) -> Unit)
  fun <T> readJsonFile(path: FsPath, deserializer: DeserializationStrategy<T>): T

  fun writeString(path: FsPath, content: String)
  fun writeAllBytes(path: FsPath, bytes: ByteArray)
  fun <T> writeJsonFile(path: FsPath, serializer: SerializationStrategy<T>, value: T)
}
