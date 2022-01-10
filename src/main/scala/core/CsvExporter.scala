package com.briefbytes.formulametrics
package core

import java.nio.file.{Files, Path, StandardOpenOption}

object CsvExporter {
  def exportToFile(obj: CsvExportable, path: Path): Unit = {
    val header = !Files.exists(path)
    try {
      Files.write(path, obj.toCsv(header).getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE)
    } catch {
      case exception: Exception => println(s"Error exporting file: $exception")
    }
  }
}
