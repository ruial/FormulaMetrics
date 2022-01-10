package com.briefbytes.formulametrics
package core

import java.nio.ByteBuffer

case class FinalClassification(position: Int,
                               laps: Int,
                               gridPosition: Int,
                               points: Int,
                               pitStops: Int,
                               resultStatus: String,
                               bestLapTime: Long,
                               totalRaceTime: Double,
                               penaltiesTime: Int,
                               penalties: Int,
                               tyreStints: Int,
                               tyreStintsActual: Array[String],
                               tyreStintsVisual: Array[String])

object FinalClassification {
  def apply(bytes: ByteBuffer, carIdx: Int): FinalClassification = {
    bytes.position(bytes.position() + 1 + carIdx * 37)
    // val totalCars = bytes.get()
    // val cars = (0 until totalCars).map(_ => { ... }); cars(carIdx)
    val position = bytes.get()
    val laps = bytes.get()
    val gridPosition = bytes.get()
    val points = bytes.get()
    val pitStops = bytes.get()
    val r = bytes.get()
    val resultStatus = r match {
      case 0 => "Invalid"
      case 1 => "Inactive"
      case 2 => "Active"
      case 3 => "Finished"
      case 4 => "Did Not Finish"
      case 5 => "Disqualified"
      case 6 => "Not Classified"
      case 7 => "Retired"
    }
    val bestLapTime = java.lang.Integer.toUnsignedLong(bytes.getInt())
    val totalRaceTime = bytes.getDouble()
    val penaltiesTime = bytes.get()
    val penalties = bytes.get()
    val tyreStints = bytes.get()
    val tyreStintsActual = new Array[Byte](8)
    val tyreStintsVisual = new Array[Byte](8)
    bytes.get(tyreStintsActual)
    bytes.get(tyreStintsVisual)
    FinalClassification(position, laps, gridPosition, points, pitStops, resultStatus,
      bestLapTime, totalRaceTime, penaltiesTime, penalties, tyreStints,
      tyreStintsActual.map(FormulaUtils.tyreActualCompound), tyreStintsVisual.map(FormulaUtils.tyreVisualCompound))
  }
}