package lgbt.mystic.syscan.tool

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.multiple
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.enum
import lgbt.mystic.syscan.PlatformConcurrentMap
import lgbt.mystic.syscan.PlatformEpochMilliseconds
import lgbt.mystic.syscan.analysis.AnalysisStep
import lgbt.mystic.syscan.artifact.Artifact
import lgbt.mystic.syscan.concurrent.TaskPool
import lgbt.mystic.syscan.frontend.SystemAnalyzer
import lgbt.mystic.syscan.frontend.SystemAnalyzerConfiguration
import lgbt.mystic.syscan.frontend.SystemAnalyzerHandler
import lgbt.mystic.syscan.io.FsPath
import lgbt.mystic.syscan.io.delete
import lgbt.mystic.syscan.io.exists
import lgbt.mystic.syscan.io.fsPath
import lgbt.mystic.syscan.io.java.toJavaPath
import lgbt.mystic.syscan.tool.output.OutputFormats
import java.io.PrintStream
import kotlin.io.path.outputStream

class AnalyzerTool : CliktCommand("System Analyzer", name = "analyze") {
  private val roots by option("--root", "-r").fsPath(mustExist = true, canBeFile = false).multiple()
  private val outputFilePath by option("--output-file-path", "-o").fsPath()
  private val skipStepList by option("--skip-step", "-S").multiple()

  private val restrictive by option("--restrictive", "-R").flag()

  private val outputFormat by option("--format", "-f").enum<OutputFormats> { it.id }.default(OutputFormats.Json)

  private val measureTimeTaken by option("--measure-time", "-M").flag()

  private val pool by requireObject<TaskPool>()

  lateinit var outputPrintStream: PrintStream

  override fun run() {
    if (outputFilePath != null && outputFilePath!!.exists()) {
      outputFilePath!!.delete()
    }

    val outputFileStream = outputFilePath?.toJavaPath()?.outputStream()
    outputPrintStream = if (outputFileStream != null) PrintStream(outputFileStream) else System.out

    val configuration = SystemAnalyzerConfiguration()
    val handler = AnalyzerHandler(this)
    configuration.taskPool = pool
    configuration.handler = handler
    val analyzer = SystemAnalyzer(configuration)
    val start = PlatformEpochMilliseconds()
    roots.forEach { root -> analyzer.submitScanRoot(root) }
    analyzer.submitLocalSystem()
    pool.await()
    val end = PlatformEpochMilliseconds()

    if (measureTimeTaken) {
      val averageTimeTakenMillis = handler.artifactTimingLog.average()
      val maximumTimeTakenMillis = handler.artifactTimingLog.max()
      val cpuTimeTakenMillis = handler.artifactTimingLog.sum()
      val realTimeTakenMillis = end - start
      val concurrencyEfficiencyFactor = cpuTimeTakenMillis.toDouble() / realTimeTakenMillis.toDouble()
      System.err.println("average time ${averageTimeTakenMillis}ms, maximum time ${maximumTimeTakenMillis}ms, cpu time ${cpuTimeTakenMillis}ms, real time ${realTimeTakenMillis}ms, concurrency efficiency $concurrencyEfficiencyFactor")
    }
  }

  class AnalyzerHandler(private val tool: AnalyzerTool) : SystemAnalyzerHandler {
    private val artifactAnalysisTimings = PlatformConcurrentMap<Artifact, Long>()
    val artifactTimingLog = mutableListOf<Long>()

    override fun onArtifactStart(artifact: Artifact) {
      artifactAnalysisTimings[artifact] = PlatformEpochMilliseconds()
    }

    override fun onArtifactEnd(artifact: Artifact) {
      val endTimeMillis = PlatformEpochMilliseconds()
      val startTimeMillis = artifactAnalysisTimings.remove(artifact)!!
      val timeTakenMillis = endTimeMillis - startTimeMillis
      synchronized(tool.outputPrintStream) {
        tool.outputFormat.format.write(tool.outputPrintStream, artifact)
      }

      synchronized(artifactTimingLog) {
        artifactTimingLog.add(timeTakenMillis)
      }
    }

    override fun onArtifactStepError(artifact: Artifact, step: AnalysisStep, e: Throwable) {
      System.err.println("ERROR while analyzing artifact $artifact in step ${step.metadataSourceKey}")
      e.printStackTrace(System.err)
    }

    override fun shouldRunStepOnArtifact(step: AnalysisStep, artifact: Artifact): Boolean =
      !tool.skipStepList.contains(step.metadataSourceKey)

    override fun acceptContextEmit(artifact: Artifact): Boolean = !tool.restrictive
    override fun acceptContextRootScan(path: FsPath): Boolean = !tool.restrictive
  }
}
