@file:Suppress("FunctionName")
package io.kexec.syscan

import io.kexec.syscan.concurrent.TaskPool
import io.kexec.syscan.concurrent.java.JavaTaskPool
import io.kexec.syscan.hash.Hash
import io.kexec.syscan.hash.java.JavaHash
import io.kexec.syscan.io.FsPath
import io.kexec.syscan.io.java.JavaPath
import io.kexec.syscan.process.ProcessSpawner
import io.kexec.syscan.process.java.JavaProcessSpawner
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
