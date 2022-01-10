package com.briefbytes.formulametrics
package core

import java.nio.ByteBuffer

// This project is compatible with 2021 udp specification
// https://forums.codemasters.com/topic/80231-f1-2021-udp-specification -
// https://forums.codemasters.com/topic/54423-f1%C2%AE-2020-udp-specification

case class PacketHeader(packetFormat: Short,
                        gameMajorVersion: Byte,
                        gameMinorVersion: Byte,
                        packetVersion: Byte,
                        packetId: String,
                        sessionUID: String,
                        sessionTime: Float,
                        frameIdentifier: Int,
                        playerCarIndex: Byte,
                        secondaryPlayerCarIndex: Byte
                       )

object PacketHeader {
  def apply(bytes: ByteBuffer): PacketHeader = {
    val packetFormat = bytes.getShort()
    val gameMajorVersion = bytes.get()
    val gameMinorVersion = bytes.get()
    val packetVersion = bytes.get()
    val packetId = bytes.get() match {
      case 1 => "Session"
      case 8 => "Final Classification"
      case 11 => "Session History"
      case _ => "Other"
    }
    val sessionUID = java.lang.Long.toUnsignedString(bytes.getLong())
    val sessionTime = bytes.getFloat()
    val frameIdentifier = bytes.getInt()
    val playerCarIndex = bytes.get()
    val secondaryPlayerCarIndex = bytes.get()
    PacketHeader(packetFormat, gameMajorVersion, gameMinorVersion, packetVersion, packetId,
      sessionUID, sessionTime, frameIdentifier, playerCarIndex, secondaryPlayerCarIndex)
  }
}
