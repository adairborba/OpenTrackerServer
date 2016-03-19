package handler

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.io.Tcp.{Received, _}

trait HandlerProps {
  def props(connection: ActorRef): Props
}

abstract class Handler(val connection: ActorRef) extends Actor with ActorLogging {

  val abort = "(?i)abort".r
  val confirmedClose = "(?i)confirmedclose".r
  val close = "(?i)close".r

  def receive: Receive = {
    case Received(data) =>
      data.utf8String.trim match {
        case abort() => connection ! Abort
        case confirmedClose() => connection ! ConfirmedClose
        case close() => connection ! Close
        case str => received(str)
      }
    case PeerClosed =>
      peerClosed()
      stop()
    case ErrorClosed =>
      errorClosed()
      stop()
    case Closed =>
      closed()
      stop()
    case ConfirmedClosed =>
      confirmedClosed()
      stop()
    case Aborted =>
      aborted()
      stop()
  }

  def peerClosed() {
    log.info("PeerClosed")
  }

  def errorClosed() {
    log.info("ErrorClosed")
  }

  def closed() {
    log.info("Closed")
  }

  def confirmedClosed() {
    log.info("ConfirmedClosed")
  }

  def aborted() {
    log.info("Aborted")
  }

  def stop() {
    log.info("Stopping")
    context stop self
  }

  def received(str: String): Unit
}

