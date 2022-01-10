package com.briefbytes.formulametrics
package core

import java.lang.Integer.toUnsignedLong
import java.lang.Short.toUnsignedInt
import java.nio.ByteBuffer

case class SessionHistory(carIdx: Int,
                          laps: Int,
                          tyreStints: Int,
                          bestLapTimeNum: Int,
                          bestSector1LapNum: Int,
                          bestSector2LapNum: Int,
                          bestSector3LapNum: Int,
                          lapHistoryData: Array[LapHistoryData],
                          tyreStintsHistoryData: Array[TyreStintHistoryData])

case class LapHistoryData(lapTimeInMs: Long,
                          sector1TimeInMS: Long,
                          sector2TimeInMS: Long,
                          sector3TimeInMS: Long,
                          lapValid: Boolean,
                          sector1Valid: Boolean,
                          sector2Valid: Boolean,
                          sector3Valid: Boolean,
                         )

case class TyreStintHistoryData(endLap: Int,
                                tyreActualCompound: String,
                                tyreVisualCompound: String)

object SessionHistory {
  def apply(bytes: ByteBuffer): SessionHistory = {
    val carIdx = bytes.get()
    val laps = bytes.get()
    val tyreStints = bytes.get()
    val bestLapTimeNum = bytes.get()
    val bestSector1LapNum = bytes.get()
    val bestSector2LapNum = bytes.get()
    val bestSector3LapNum = bytes.get()
    val lapHistory = (0 until laps).map(_ => {
      val lapTimeInMs = toUnsignedLong(bytes.getInt())
      val sector1TimeInMS = toUnsignedInt(bytes.getShort())
      val sector2TimeInMS = toUnsignedInt(bytes.getShort())
      val sector3TimeInMS = toUnsignedInt(bytes.getShort())
      val lapValidBitFlags = bytes.get()
      val lapValid = (lapValidBitFlags & 0x01) != 0
      val sector1Valid = (lapValidBitFlags & 0x02) != 0
      val sector2Valid = (lapValidBitFlags & 0x04) != 0
      val sector3Valid = (lapValidBitFlags & 0x08) != 0
      LapHistoryData(lapTimeInMs, sector1TimeInMS, sector2TimeInMS, sector3TimeInMS,
        lapValid, sector1Valid, sector2Valid, sector3Valid)
    }).filter(_.lapTimeInMs > 0).toArray
    bytes.position(bytes.position() + 11 * (100 - laps))
    val tyresHistory = (0 until tyreStints).map(_ => {
      val endLap = bytes.get()
      val tyreActualCompound = FormulaUtils.tyreActualCompound(bytes.get())
      val tyreVisualCompound = FormulaUtils.tyreVisualCompound(bytes.get())
      TyreStintHistoryData(endLap, tyreActualCompound, tyreVisualCompound)
    }).toArray
    SessionHistory(carIdx, laps, tyreStints, bestLapTimeNum, bestSector1LapNum,
      bestSector2LapNum, bestSector3LapNum, lapHistory, tyresHistory)
  }
}
