package com.briefbytes.formulametrics
package core

import java.nio.ByteBuffer

case class Session(weather: String,
                   trackTemperature: Int,
                   airTemperature: Int,
                   totalLaps: Int,
                   trackLength: Int,
                   sessionType: String,
                   trackId: String,
                   formula: String,
                   isSpectating: Boolean,
                   networkGame: String)


object Session {
  def apply(bytes: ByteBuffer): Session = {
    val weather = bytes.get() match {
      case 0 => "clear"
      case 1 => "light cloud"
      case 2 => "overcast"
      case 3 => "light rain"
      case 4 => "heavy rain"
      case 5 => "storm"
      case other => s"unknown: $other"
    }
    val trackTemperature = bytes.get()
    val airTemperature = bytes.get()
    val totalLaps = java.lang.Byte.toUnsignedInt(bytes.get()) // has invalid value on qualifying
    val trackLength = bytes.getShort()
    val sessionType = bytes.get() match {
      case 0 => "unknown"
      case 1 => "P1"
      case 2 => "P2"
      case 3 => "P3"
      case 4 => "Short P"
      case 5 => "Q1"
      case 6 => "Q2"
      case 7 => "Q3"
      case 8 => "Short Q"
      case 9 => "OSQ"
      case 10 => "R"
      case 11 => "R2"
      case 12 => "R3"
      case 13 => "Time Trial"
    }
    val trackId = bytes.get() match {
      case 0 => "Melbourne"
      case 1 => "Paul Ricard"
      case 2 => "Shanghai"
      case 3 => "Sakhir (Bahrain)"
      case 4 => "Catalunya"
      case 5 => "Monaco"
      case 6 => "Montreal"
      case 7 => "Silverstone"
      case 8 => "Hockenheim"
      case 9 => "Hungaroring"
      case 10 => "Spa"
      case 11 => "Monza"
      case 12 => "Singapore"
      case 13 => "Suzuka"
      case 14 => "Abu Dhabi"
      case 15 => "Texas"
      case 16 => "Brazil"
      case 17 => "Austria"
      case 18 => "Sochi"
      case 19 => "Mexico"
      case 20 => "Baku (Azerbaijan)"
      case 21 => "Sakhir Short"
      case 22 => "Silverstone Short"
      case 23 => "Texas Short"
      case 24 => "Suzuka Short"
      case 25 => "Hanoi"
      case 26 => "Zandvoort"
      case 27 => "Imola"
      case 28 => "Portimão"
      case 29 => "Jeddah"
    }
    val formula = bytes.get() match {
      case 0 => "F1 Modern"
      case 1 => "F1 Classic"
      case 2 => "F2"
      case 3 => "F1 Generic"
    }
    // discard some less interesting bytes to get spectating and network info
    bytes.position(bytes.position() + 6)
    val isSpectating = bytes.get() == 1
    bytes.position(bytes.position() + 3 + 21 * 5 + 1)
    val networkGame = if (bytes.get() == 0) "Offline" else "Online"
    Session(weather, trackTemperature, airTemperature, totalLaps, trackLength,
      sessionType, trackId, formula, isSpectating, networkGame)
  }
}