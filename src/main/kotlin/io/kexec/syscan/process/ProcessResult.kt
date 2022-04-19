package io.kexec.syscan.process

import java.nio.charset.StandardCharsets

class ProcessResult(
  val exitCode: Int,
  val stdoutBytes: ByteArray,
  val stderrBytes: ByteArray
) {
  val stdoutAsString by lazy {
    stdoutBytes.toString(StandardCharsets.UTF_8)
  }

  val stderrAsString by lazy {
    stderrBytes.toString(StandardCharsets.UTF_8)
  }
}
