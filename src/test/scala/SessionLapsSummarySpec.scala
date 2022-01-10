package com.briefbytes.formulametrics

import core._

class SessionLapsSummarySpec extends UnitSpec {

  // More testing examples in https://github.com/hhimanshu/Unit-Testing-In-Scala/tree/6.Fixtures/src/test/scala

  val raceID = "test"
  val session = Session("storm", 15, 25, 5, 5000, "R", "Brazil", "F1 Modern", false, "Offline")
  val otherSession = Session("clear", 20, 30, 5, 5000, "R", "Brazil", "F1 Modern", false, "Offline")
  val bestLap = LapHistoryData(86700, 28900, 28900, 28900, true, true, true, true)
  val otherLaps = LapHistoryData(90000, 30000, 30000, 30000, true, true, true, true)
  val tyreStints = Array(TyreStintHistoryData(2, "C4", "soft"), TyreStintHistoryData(-1, "C3", "medium"))
  val lapsHistory = Array(otherLaps, otherLaps, bestLap, otherLaps, otherLaps)
  val history = SessionHistory(1, 5, 2, 3, 3, 3, 3, lapsHistory, tyreStints)
  val when = java.time.LocalDateTime.of(2021, 12, 1, 16, 30, 15)
  val summary = new SessionLapsSummary(raceID, Map(1 -> session, 2 -> session, 3 -> otherSession, 5 -> session), session, history, when)
  val header = "Race ID,Date,Weather,Track Temperature,Air Temperature,Total Laps,Track Length,Session Type,Track,Formula,Network," +
    "Lap,Lap Time,Sector1 Time,Sector2 Time,Sector3 Time,Lap Valid,Sector1 Valid,Sector2 Valid,Sector3 Valid"
  val rows = "test,2021-12-01T16:30,storm,15,25,5,5000,R,Brazil,F1 Modern,Offline,1,01:30.000,00:30.000,00:30.000,00:30.000,true,true,true,true,soft" + "\n" +
    "test,2021-12-01T16:30,storm,15,25,5,5000,R,Brazil,F1 Modern,Offline,2,01:30.000,00:30.000,00:30.000,00:30.000,true,true,true,true,soft" + "\n" +
    "test,2021-12-01T16:30,clear,20,30,5,5000,R,Brazil,F1 Modern,Offline,3,01:26.700,00:28.900,00:28.900,00:28.900,true,true,true,true,medium" + "\n" +
    "test,2021-12-01T16:30,storm,15,25,5,5000,R,Brazil,F1 Modern,Offline,4,01:30.000,00:30.000,00:30.000,00:30.000,true,true,true,true,medium" + "\n" +
    "test,2021-12-01T16:30,storm,15,25,5,5000,R,Brazil,F1 Modern,Offline,5,01:30.000,00:30.000,00:30.000,00:30.000,true,true,true,true,medium" + "\n"

  behavior of "SessionLapsSummary"

  it should "build CSV output without header" in {
    summary.toCsv(false) should equal(rows)
  }

  it should "build CSV output with header" in {
    summary.toCsv(true) should equal(header + "\n" + rows)
  }

}
