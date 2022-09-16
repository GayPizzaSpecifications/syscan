package lgbt.mystic.syscan.frontend

import lgbt.mystic.syscan.concurrent.DirectTaskPool
import lgbt.mystic.syscan.concurrent.TaskPool

class SystemAnalyzerConfiguration {
  var taskPool: TaskPool = DirectTaskPool
  var handler: SystemAnalyzerHandler = NullSystemAnalyzerHandler
}
