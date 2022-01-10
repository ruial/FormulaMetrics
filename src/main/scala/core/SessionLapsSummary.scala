package com.briefbytes.formulametrics
package core

import java.time.LocalDateTime

class SessionLapsSummary(id: String, sessions: Map[Int, Session], originalSession: Session, history: SessionHistory,
                         date: LocalDateTime = java.time.LocalDateTime.now()) extends CsvExportable {

  override def toCsv(header: Boolean = false): String = {
    val prefix = if (header) {
      Array("Race ID", "Date", "Weather", "Track Temperature", "Air Temperature", "Total Laps", "Track Length",
        "Session Type", "Track", "Formula", "Network", "Lap", "Lap Time", "Sector1 Time",
        "Sector2 Time", "Sector3 Time", "Lap Valid", "Sector1 Valid",
        "Sector2 Valid", "Sector3 Valid").mkString(Separator) + "\n"
    } else ""
    prefix + toCsv()
  }

  private def toCsv(): String = {
    val lapTyres = getLapTyres()
    history.lapHistoryData.map(data => {
      data.productIterator.map {
        case l: Long => FormulaUtils.prettyLapTime(l)
        case value => value.toString
      }.mkString(Separator)
    }).zipWithIndex.map { case (str, idx) =>
      val lapNum = idx + 1
      // a small hack to get the ordered case class values, could also get keys with productElementNames for the header
      // https://stackoverflow.com/a/52728362
      val sessionStr = sessions.getOrElse(lapNum, originalSession).productIterator.filter(_ match {
        case _: Boolean => false // this removes isSpectating
        case _ => true
      }).mkString(Separator)
      id + Separator + FormulaUtils.dateFormat(date) + Separator +
        sessionStr + Separator + lapNum + Separator + str + Separator + lapTyres(lapNum)
    }.mkString("\n") + "\n"
  }

  private def getLapTyres(): Map[Int, String] = {
    var result: Map[Int, String] = Map()
    var currentLap = 1
    history.tyreStintsHistoryData.foreach(data => {
      val endLap = if (data.endLap == -1) history.laps else data.endLap
      while (currentLap <= endLap) {
        result = result + (currentLap -> data.tyreVisualCompound)
        currentLap += 1
      }
    })
    result
  }

}
