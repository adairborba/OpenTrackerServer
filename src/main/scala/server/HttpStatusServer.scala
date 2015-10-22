package server

import akka.actor.{Props, _}
import akka.io.IO
import akka.util.Timeout
import api.MongoApi
import spray.can.Http
import spray.http.HttpMethods._
import spray.http._
import util.ConfExtension

import scala.concurrent.duration._


object HttpStatusServer {
  def props(): Props = Props(classOf[HttpStatusServer])

  val system = ActorSystem("server")
  val interface = ConfExtension(system).interface
  val port = ConfExtension(system).httpPort

  println("Starting HTTP Server at " + interface + ":" + port)
}

class HttpStatusServer() extends Server with ActorLogging {

  import context.system

  implicit val timeout: Timeout = Timeout(5 seconds)

  IO(Http) ! Http.Bind(self, interface = HttpStatusServer.interface, port = HttpStatusServer.port)

  override def receive = {
    // when a new connection comes in we register ourselves as the connection handler
    case _: Http.Connected => sender ! Http.Register(self)

    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      sender ! HttpResponse(entity = "UP")

    case HttpRequest(GET, Uri.Path("/test"), _, _, _) => {
      val status = MongoApi.getStatus()
      sender ! HttpResponse(entity = "STATUS=OK\n{" + status + "}")
    }

    case Timedout(HttpRequest(method, uri, _, _, _)) =>
      sender ! HttpResponse(
        status = 500,
        entity = "The " + method + " request to '" + uri + "' has timed out..."
      )

    case _: HttpRequest => sender ! HttpResponse(status = 404, entity = "Unknown resource!")
  }
}