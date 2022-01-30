package io.kexec.syscan

import io.kexec.syscan.concurrent.TaskPool
import io.kexec.syscan.concurrent.java.JavaTaskPool
import io.kexec.syscan.hash.Hash
import io.kexec.syscan.hash.JavaHash
import io.kexec.syscan.io.FsPath
import io.kexec.syscan.io.java.JavaPath
import java.nio.file.Path
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap

actual fun PlatformPath(path: String): FsPath = JavaPath(Path.of(path))
actual fun <K, V> PlatformConcurrentMap(): MutableMap<K, V> = ConcurrentHashMap()
actual fun PlatformTaskPool(concurrency: Int): TaskPool = JavaTaskPool(concurrency)
actual fun PlatformHash(name: String): Hash = JavaHash(MessageDigest.getInstance(name))
