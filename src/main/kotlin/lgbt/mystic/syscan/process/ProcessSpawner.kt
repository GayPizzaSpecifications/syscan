package lgbt.mystic.syscan.process

import lgbt.mystic.syscan.io.FsPath

interface ProcessSpawner {
  fun execute(executable: String, args: List<String>, workingDirectory: FsPath? = null): ProcessResult
}
