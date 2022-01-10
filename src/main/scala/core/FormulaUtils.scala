package com.briefbytes.formulametrics
package core

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object FormulaUtils {

  def dateFormat(date: LocalDateTime): String = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm").format(date)

  def prettyLapTime(totalMs: Long): String = {
    val totalSecs = totalMs / 1000
    val ms = totalMs % 1000
    val minutes = totalSecs / 60
    val seconds = totalSecs % 60
    // get ms in excel: https://stackoverflow.com/questions/8183698/how-do-i-convert-hhmmss-000-to-milliseconds-in-excel
    String.format("%02d:%02d.%03d", minutes, seconds, ms)
  }

  def tyreActualCompound(b: Byte): String = b match {
    case 16 => "C5"
    case 17 => "C4"
    case 18 => "C3"
    case 19 => "C2"
    case 20 => "C1"
    case 7 => "inter"
    case 8 => "wet"
    case 9 => "dry"
    case 10 => "wet"
    case 11 => "super soft"
    case 12 => "soft"
    case 13 => "medium"
    case 14 => "hard"
    case 15 => "wet"
    case _ => "NA"
  }

  def tyreVisualCompound(b: Byte): String = b match {
    case 16 => "soft"
    case 17 => "medium"
    case 18 => "hard"
    case 7 => "inter"
    case 8 => "wet"
    case 15 => "wet"
    case 19 => "super soft"
    case 20 => "soft"
    case 21 => "medium"
    case 22 => "hard"
    case _ => "NA"
  }

}
