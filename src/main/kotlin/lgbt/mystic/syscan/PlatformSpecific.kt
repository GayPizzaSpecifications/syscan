@file:Suppress("FunctionName")
package lgbt.mystic.syscan

import lgbt.mystic.syscan.concurrent.TaskPool
import lgbt.mystic.syscan.concurrent.java.JavaTaskPool
import lgbt.mystic.syscan.hash.Hash
import lgbt.mystic.syscan.hash.java.JavaHash
import lgbt.mystic.syscan.io.FsPath
import lgbt.mystic.syscan.io.java.JavaPath
import lgbt.mystic.syscan.process.ProcessExecutor
import lgbt.mystic.syscan.process.java.JavaProcessSpawner
import java.io.File
import java.nio.file.Path
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap
import kotlin.io.path.absolute

fun PlatformInit() {}
fun PlatformPath(path: String): FsPath = JavaPath(Path.of(path))
fun PlatformEpochMilliseconds(): Long = System.currentTimeMillis()
fun PlatformPathSeparator(): Char = File.pathSeparatorChar
fun PlatformCurrentWorkingDirectory(): FsPath = JavaPath(Path.of(".").absolute())
fun <K, V> PlatformConcurrentMap(): MutableMap<K, V> = ConcurrentHashMap()
fun PlatformTaskPool(concurrency: Int): TaskPool = JavaTaskPool(concurrency)
fun PlatformHash(name: String): Hash = JavaHash(MessageDigest.getInstance(name), name)
val PlatformProcessSpawner: ProcessExecutor = JavaProcessSpawner
