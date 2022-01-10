package com.briefbytes.formulametrics

import core._

import akka.actor.{Actor, ActorRef, Props}
import akka.io.{IO, Udp}

import java.net.InetSocketAddress
import java.nio.file.Path
import java.nio.{ByteBuffer, ByteOrder}

class ListenerActor(local: InetSocketAddress, dir: Path) extends Actor {

  import context.system

  IO(Udp) ! Udp.Bind(self, local)

  var currentSessionID = ""
  var currentSession: Option[Session] = None
  var currentLap = 0
  var lapHistory: Option[SessionHistory] = None
  var lapSessions: Map[Int, Session] = Map()

  def receive = {
    case Udp.Bound(local) =>
      context.become(ready(sender()))
  }

  def ready(socket: ActorRef): Receive = {
    case Udp.Received(data, remote) =>
      val bytes = data.toByteBuffer.order(ByteOrder.LITTLE_ENDIAN)
      val processed = PacketHeader(bytes)
      processed.packetFormat match {
        case 2021 => processPacketHeader(processed, bytes)
        case value => println(s"Unsupported packet format: $value. Required: 2021")
      }
    // socket ! Udp.Send(data, remote) // example server echoes back
    case Udp.Unbind => socket ! Udp.Unbind
    case Udp.Unbound => context.stop(self)
  }

  private def processPacketHeader(header: PacketHeader, bytes: ByteBuffer): Unit = {
    header.packetId match {
      case "Session" => {
        val session = Session(bytes)
        if (currentSessionID != header.sessionUID) {
          println(s"Previous session: $currentSessionID")
          currentSessionID = header.sessionUID
          currentSession = Some(session)
          lapHistory = None
          lapSessions = Map()
          currentLap = 0
          println(s"New session: $currentSessionID")
          println(session)
        }
        // some session data like temperature/weather can change between laps
        lapSessions = lapSessions + (currentLap -> session)
      }
      case "Final Classification" => {
        if (header.playerCarIndex == -1) {
          println("Invalid player car index, not saving final classification")
          return
        }

        if (currentSession.isEmpty || lapHistory.isEmpty) {
          println("No additional data to export, not saving final classification")
          return
        }

        val session = currentSession.get
        // when the car index is -1 we are spectating but not always for some reason, need to check session also
        if (session.isSpectating) {
          println("Spectating, not saving final classification")
          return
        }

        val finalClassification = FinalClassification(bytes, header.playerCarIndex)
        println("My final classification:")
        println(finalClassification)

        // have fallback current/initial session in case the application is started mid-race
        val lapSummary = new SessionLapsSummary(currentSessionID, lapSessions, session, lapHistory.get)
        println(s"\nExporting session $currentSessionID\nLaps:")
        println(lapSummary.toCsv(true))
        CsvExporter.exportToFile(lapSummary, dir.resolve(session.trackId + "-" + session.sessionType + "-laps.csv"))

        println("Classification:")
        val finalSummary = new FinalClassificationSummary(currentSessionID, finalClassification)
        println(finalSummary.toCsv(true))
        CsvExporter.exportToFile(finalSummary, dir.resolve(session.trackId + "-" + session.sessionType + "-classification.csv"))
        currentSession = None
      }
      case "Session History" => {
        val sessionHistory = SessionHistory(bytes)
        if (header.playerCarIndex == sessionHistory.carIdx) {
          lapHistory = Some(sessionHistory)
          if (currentLap != sessionHistory.laps) {
            currentLap = sessionHistory.laps
            // alternatively could export lap times here
            println(s"Session history ${header.sessionUID} received, current lap: $currentLap")
          }
        }
      }
      case _ =>
    }
  }
}

object ListenerActor {
  def apply(local: InetSocketAddress, dir: Path) = Props(classOf[ListenerActor], local, dir)
}