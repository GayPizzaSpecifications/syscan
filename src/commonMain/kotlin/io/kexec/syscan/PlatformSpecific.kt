@file:Suppress("FunctionName")
package io.kexec.syscan

import io.kexec.syscan.concurrent.TaskPool
import io.kexec.syscan.hash.Hash
import io.kexec.syscan.io.FsPath

expect fun PlatformPath(path: String): FsPath
expect fun <K, V> PlatformConcurrentMap(): MutableMap<K, V>
expect fun PlatformTaskPool(concurrency: Int): TaskPool
expect fun PlatformHash(name: String): Hash
