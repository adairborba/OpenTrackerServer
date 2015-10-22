package api

import akka.actor.ActorSystem
import spray.client.pipelining._
import spray.http.HttpMethods._
import spray.http.Uri.apply
import spray.http.{HttpMethod, HttpRequest, _}

object Api extends Api

class Api {

  implicit val system = ActorSystem("api-spray-client")

  import system.dispatcher

  //To be able to mock
  def sendAndReceive = sendReceive

  def createHttpRequest(uri: String,
                        method: HttpMethod,
                        data: String) =

    HttpRequest(method = method,
      uri = uri,
      entity = HttpEntity(data))

  /**
    * Makes HTTP request
    * @param uri
    * @param data
    * @param method
    * @return
    */
  def httpRequest(uri: String,
                  method: HttpMethod = GET,
                  data: String = "") = {
    val pipeline = sendAndReceive
    pipeline {
      createHttpRequest(uri, method, data)
    }
  }
}
