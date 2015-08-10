package handler

import akka.util.ByteString
import api.Api
import spray.http.HttpMethods._

import akka.io.Tcp.Write
import akka.actor.{Props, ActorRef}
import util.ConfExtension

object ApiHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[ApiHandler], connection)
}

object ApiHandler {
  private val newLine = ByteString("\n")
}

class ApiHandler(connection: ActorRef) extends Handler(connection) {

  import context.dispatcher

  /**
   * Makes an api request to GeoLink Server
   */
  def received(data: String) = {
    println("--->" + data)

    val httpData: String = buildHttpString(data)
    val uri = ConfExtension(context.system).apiUrl + httpData
    Api.httpRequest(method = GET, uri = uri) map {
      response => {
        println("Response:" + response.entity.asString)
        respond("OK")
      }
    }
  }

  /**
   * Build HTTP string from RAW comma separated data
   * @param data
   */

  def buildHttpString(data: String): String = {
    println(s"Converting\n data $data")
    val dataArray = data.split(",")
    val imei: String = dataArray(0)
    val key: String = dataArray(1)
    val d: String = dataArray(2) + "," + dataArray(3)
    val gpsData = dataArray.toIndexedSeq.drop(4).dropRight(2).mkString(",")
    val httpData = s"imei=$imei&key=$key&d=$d[$gpsData]{}"
    httpData
  }

  /**
   * Sends given data over connection to client, appending a newline
   * @param response
   */
  def respond(response: String) {
    connection ! Write(ByteString(response) ++ ApiHandler.newLine)
  }
}
