package io.kexec.syscan.process

import io.kexec.syscan.io.FsPath

interface ProcessSpawner {
  fun execute(executable: String, args: List<String>, workingDirectory: FsPath? = null): ProcessResult
}
