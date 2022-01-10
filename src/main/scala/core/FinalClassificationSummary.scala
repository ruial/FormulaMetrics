package com.briefbytes.formulametrics
package core

import java.time.LocalDateTime

class FinalClassificationSummary(id: String, classification: FinalClassification,
                                 date: LocalDateTime = java.time.LocalDateTime.now()) extends CsvExportable {

  override def toCsv(header: Boolean = false): String = {
    val prefix = if (header) {
      Array("Race ID", "Date", "Position", "Laps", "Grid Position", "Points", "Pit Stops",
        "Result Status", "Best Lap Time", "Total Race Time (s)", "Penalties Time (s)",
        "Penalties", "Tyre Stints").mkString(Separator) + "\n"
    } else ""
    prefix + toCsv()
  }

  private def toCsv(): String = {
    val classificationStr = classification.productIterator.filter(_ match {
      case _: Array[String] => false // remove tyre stints
      case _ => true
    }).map {
      case l: Long => FormulaUtils.prettyLapTime(l)
      case value => value.toString
    }.mkString(Separator)
    id + Separator + FormulaUtils.dateFormat(date) + Separator + classificationStr + "\n"
  }
}
