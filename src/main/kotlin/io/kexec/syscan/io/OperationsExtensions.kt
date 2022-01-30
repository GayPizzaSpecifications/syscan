package io.kexec.syscan.io

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy

fun FsPath.exists(): Boolean =
  operations.exists(this)

fun FsPath.isDirectory(): Boolean =
  operations.isDirectory(this)

fun FsPath.isRegularFile(): Boolean =
  operations.isRegularFile(this)

fun FsPath.isSymbolicLink(): Boolean =
  operations.isSymbolicLink(this)

fun FsPath.isReadable(): Boolean =
  operations.isReadable(this)

fun FsPath.isWritable(): Boolean =
  operations.isWritable(this)

fun FsPath.isExecutable(): Boolean =
  operations.isExecutable(this)

fun FsPath.list(): Sequence<FsPath> =
  operations.list(this)

fun FsPath.walk(): Sequence<FsPath> =
  operations.walk(this)

fun FsPath.visit(visitor: FsPathVisitor): Unit =
  operations.visit(this, visitor)

fun FsPath.readString(): String =
  operations.readString(this)

fun FsPath.readAllBytes(): ByteArray =
  operations.readAllBytes(this)

fun FsPath.readBytesChunked(block: (ByteArray, Int) -> Unit) =
  operations.readBytesChunked(this, block)

fun <T> FsPath.readJsonFile(deserializer: DeserializationStrategy<T>): T =
  operations.readJsonFile(this, deserializer)

fun FsPath.writeString(content: String): Unit =
  operations.writeString(this, content)

fun FsPath.writeAllBytes(bytes: ByteArray): Unit =
  operations.writeAllBytes(this, bytes)

fun <T> FsPath.writeJsonFile(serializer: SerializationStrategy<T>, value: T): Unit =
  operations.writeJsonFile(this, serializer, value)
