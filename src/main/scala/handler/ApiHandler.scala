package handler

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.io.Tcp.Write
import akka.util.ByteString
import api.{Api, MongoApi, MqttApi}
import spray.http.HttpMethods._
import util.ConfExtension

import scala.concurrent.ExecutionContext.Implicits.global

object ApiHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[ApiHandler], connection)
}

object ApiHandler {
  private val newLine = ByteString("\n")

  val system = ActorSystem("server")
  val apiUri = ConfExtension(system).apiUrl


}

class ApiHandler(connection: ActorRef) extends Handler(connection) {

  /**
    * Makes an api request to GeoLink Server
    */
  def received(data: String) = {
    val now = Api.getCurrentDate()

    println("--->" + now + " " + data)

    MongoApi.insertDataIntoDB(now, data)

    MqttApi.sendData(MqttApi.prepareMessage(now, data), "owntracks/cars/vw")

    val urlWithData: String = ApiHandler.apiUri + Api.buildHttpString(now, data)
    Api.httpRequest(method = GET, uri = urlWithData) map {
      response => {
        println("Response:" + response.entity.asString)
        respond("OK")
      }
    }
  }





  /**
    * Sends given data over connection to client, appending a newline
    * @param response
    */
  def respond(response: String) {
    connection ! Write(ByteString(response) ++ ApiHandler.newLine)
  }
}
