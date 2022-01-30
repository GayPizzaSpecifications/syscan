package io.kexec.syscan.io.java

import io.kexec.syscan.io.FsPath
import java.nio.file.Path

fun Path.toFsPath(): FsPath = JavaPath(this)
fun FsPath.toJavaPath(): Path {
  return if (this is JavaPath) {
    javaPath
  } else {
    Path.of(fullPathString)
  }
}
