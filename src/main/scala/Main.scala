import akka.actor.{ Props, ActorSystem }

import handler.{ApiHandlerProps}
import server.TcpServer



object MainWithApiHandler extends App {
  val system = ActorSystem("server")
  val service = system.actorOf(TcpServer.props(ApiHandlerProps), "ServerActor")
}