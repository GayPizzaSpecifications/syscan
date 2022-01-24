package io.kexec.syscan.io.java

import io.kexec.syscan.io.FsOperations
import io.kexec.syscan.io.FsPath
import io.kexec.syscan.io.FsPathVisitor
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import java.nio.file.Files
import kotlin.streams.asSequence

object JavaFsOperations : FsOperations {
  override fun exists(path: FsPath): Boolean = Files.exists(path.toJavaPath())
  override fun isDirectory(path: FsPath): Boolean = Files.isDirectory(path.toJavaPath())
  override fun isRegularFile(path: FsPath): Boolean = Files.isRegularFile(path.toJavaPath())
  override fun isSymbolicLink(path: FsPath): Boolean = Files.isSymbolicLink(path.toJavaPath())
  override fun isReadable(path: FsPath): Boolean = Files.isReadable(path.toJavaPath())
  override fun isWritable(path: FsPath): Boolean = Files.isWritable(path.toJavaPath())
  override fun isExecutable(path: FsPath): Boolean = Files.isExecutable(path.toJavaPath())

  override fun list(path: FsPath): Sequence<FsPath> = Files.list(path.toJavaPath()).asSequence().map { it.toFsPath() }

  override fun walk(path: FsPath): Sequence<FsPath> = Files.walk(path.toJavaPath()).asSequence().map { it.toFsPath() }
  override fun visit(path: FsPath, visitor: FsPathVisitor) =
    Files.walkFileTree(path.toJavaPath(), JavaFsPathVisitorAdapter(visitor)).run {}

  override fun readString(path: FsPath): String = Files.readString(path.toJavaPath())
  override fun readAllBytes(path: FsPath): ByteArray = Files.readAllBytes(path.toJavaPath())
  override fun <T> readJsonFile(path: FsPath, deserializer: DeserializationStrategy<T>): T =
    Json.decodeFromString(deserializer, readString(path))

  override fun writeString(path: FsPath, content: String): Unit = Files.writeString(path.toJavaPath(), content).run {}
  override fun writeAllBytes(path: FsPath, bytes: ByteArray): Unit = Files.write(path.toJavaPath(), bytes).run {}
  override fun <T> writeJsonFile(path: FsPath, serializer: SerializationStrategy<T>, value: T) =
    writeString(path, Json.encodeToString(serializer, value))
}
