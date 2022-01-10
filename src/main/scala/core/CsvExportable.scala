package com.briefbytes.formulametrics
package core

trait CsvExportable {

  val Separator = ","

  def toCsv(header: Boolean): String
}
