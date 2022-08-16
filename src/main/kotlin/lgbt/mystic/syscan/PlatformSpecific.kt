@file:Suppress("FunctionName")
package lgbt.mystic.syscan

import lgbt.mystic.syscan.concurrent.TaskPool
import lgbt.mystic.syscan.concurrent.java.JavaTaskPool
import lgbt.mystic.syscan.hash.Hash
import lgbt.mystic.syscan.hash.java.JavaHash
import lgbt.mystic.syscan.io.FsPath
import lgbt.mystic.syscan.io.java.JavaPath
import lgbt.mystic.syscan.process.ProcessSpawner
import lgbt.mystic.syscan.process.java.JavaProcessSpawner
import java.nio.file.Files
import java.nio.file.Path
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap

fun PlatformInit() {}
fun PlatformPath(path: String): FsPath = JavaPath(Path.of(path))
fun PlatformCreateTempDir(): FsPath = JavaPath(Files.createTempDirectory("ksyscan-"))
fun <K, V> PlatformConcurrentMap(): MutableMap<K, V> = ConcurrentHashMap()
fun PlatformTaskPool(concurrency: Int): TaskPool = JavaTaskPool(concurrency)
fun PlatformHash(name: String): Hash = JavaHash(MessageDigest.getInstance(name))
val PlatformProcessSpawner: ProcessSpawner = JavaProcessSpawner
