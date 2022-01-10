package com.briefbytes.formulametrics

import akka.actor.{ActorRef, ActorSystem}

import java.io.File
import java.net.InetSocketAddress
import java.nio.file.Paths

object FormulaMetrics extends App {

  val (local, dir) = args match {
    case Array(hostname, port) => (new InetSocketAddress(hostname, port.toInt), ".")
    case Array(hostname, port, dir) => (new InetSocketAddress(hostname, port.toInt), dir)
    case _ => (new InetSocketAddress("localhost", 20777), ".")
  }

  val dirPath = Paths.get(dir).toAbsolutePath.normalize
  if (!new File(dirPath.toString).isDirectory) {
    println(s"Specified path $dirPath is not a directory or does not exist")
    sys.exit(1)
  }

  println(s"F1 2021 metrics exporter listening on $local and exporting to $dirPath")

  // https://aknay.github.io/2018/01/18/akka-udp-messaging.html
  // https://doc.akka.io/docs/akka/current/io-udp.html
  val system = ActorSystem("FActorSystem")
  val udp: ActorRef = system.actorOf(ListenerActor(local, dirPath), name = "Udp")
}
