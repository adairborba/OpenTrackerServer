package server

import java.net.InetSocketAddress

import akka.actor.{ActorLogging, ActorSystem, Props}
import akka.io.{IO, Tcp}
import handler._
import util.ConfExtension


object TcpServer {
  def props(handlerProps: HandlerProps): Props = Props(classOf[TcpServer], handlerProps)

  val system = ActorSystem("server")
  val interface = ConfExtension(system).interface
  val port = ConfExtension(system).appPort
  println("Starting TCP Server at " + interface + ":" + port)
}

class TcpServer(handlerProps: HandlerProps) extends Server with ActorLogging {

  import context.system


  IO(Tcp) ! Tcp.Bind(self, new InetSocketAddress(TcpServer.interface, TcpServer.port))

  override def receive = {
    case Tcp.CommandFailed(_: Tcp.Bind) => context stop self

    case Tcp.Connected(remote, local) =>
      val handler = context.actorOf(handlerProps.props(sender))
      sender ! Tcp.Register(handler)
  }
}
