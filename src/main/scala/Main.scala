import akka.actor.ActorSystem
import api.{MongoApi, MqttApi}
import handler.ApiHandlerProps
import server.{HttpStatusServer, TcpServer}


object MainWithApiHandler extends App {
  val system = ActorSystem("server")

  MongoApi.getStatus()

  val tcpService = system.actorOf(TcpServer.props(ApiHandlerProps), "TcpServer")
  val httpService = system.actorOf(HttpStatusServer.props(), "HttpStatusServer")

  MqttApi.sendData("Server Started", "opentracker/server/status")
}