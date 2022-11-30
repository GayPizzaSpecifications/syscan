package lgbt.mystic.syscan.process.java

import com.zaxxer.nuprocess.NuAbstractProcessHandler
import com.zaxxer.nuprocess.NuProcess
import com.zaxxer.nuprocess.NuProcessBuilder
import lgbt.mystic.syscan.io.java.toJavaPath
import lgbt.mystic.syscan.process.ExecutionJob
import lgbt.mystic.syscan.process.ProcessExecutor
import lgbt.mystic.syscan.process.ProcessResult
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit

object JavaProcessSpawner : ProcessExecutor {
  override fun execute(job: ExecutionJob): ProcessResult {
    val builder = NuProcessBuilder(job.expandCommandArguments())

    for ((key, value) in job.environment) {
      builder.environment()[key] = value
    }

    builder.setCwd(job.workingDirectoryPath.toJavaPath())
    val handler = BufferedProcessHandler()
    builder.setProcessListener(handler)
    val process = builder.start()
    val exitCode = process.waitFor(0, TimeUnit.SECONDS)
    val result = handler.toProcessResult(exitCode)
    process.destroy(true)
    return result
  }

  class BufferedProcessHandler : NuAbstractProcessHandler() {
    private val stdoutByteStream = ByteArrayOutputStream()
    private val stderrByteStream = ByteArrayOutputStream()

    lateinit var process: NuProcess

    override fun onStart(nuProcess: NuProcess) {
      process = nuProcess
    }

    override fun onStdinReady(buffer: ByteBuffer): Boolean {
      process.closeStdin(true)
      return false
    }

    override fun onStdout(buffer: ByteBuffer, closed: Boolean) {
      val remaining = buffer.remaining()
      val bytes = ByteArray(remaining)
      buffer.get(bytes)
      stdoutByteStream.writeBytes(bytes)
    }

    override fun onStderr(buffer: ByteBuffer, closed: Boolean) {
      val remaining = buffer.remaining()
      val bytes = ByteArray(remaining)
      buffer.get(bytes)
      stdoutByteStream.writeBytes(bytes)
    }

    fun toProcessResult(exitCode: Int): ProcessResult = ProcessResult(
      exitCode,
      stdoutByteStream.toByteArray(),
      stderrByteStream.toByteArray()
    )
  }
}
