package handler

import akka.util.ByteString
import api.Api
import spray.http.HttpMethods._

import akka.io.Tcp.Write
import akka.actor.{ActorSystem, Props, ActorRef}
import util.ConfExtension

import scala.concurrent.Future
import reactivemongo.bson.BSONDocument
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.collections.bson.BSONCollection

import scala.util.{Success, Failure}

import reactivemongo.api._
import scala.concurrent.ExecutionContext.Implicits.global

object ApiHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[ApiHandler], connection)
}

object ApiHandler {
  private val newLine = ByteString("\n")

  val system = ActorSystem("server")
  val driver = new MongoDriver
  val moncon = driver.connection(List(ConfExtension(system).db))

  // Gets a reference to the database "plugin"
  val db = moncon("open-tracker")
  val coll = db[BSONCollection]("points")
}

class ApiHandler(connection: ActorRef) extends Handler(connection) {
  /**
   * Makes an api request to GeoLink Server
   */
  def received(data: String) = {
    println("--->" + data)

    insertDataIntoDB(data)

    val httpData: String = buildHttpString(data)
    val uri = ConfExtension(context.system).apiUrl + httpData

    Api.httpRequest(method = GET, uri = uri) map {
      response => {
        println("Response:" + response.entity.asString)
        respond("OK")
      }
    }
  }

  def insertDataIntoDB(data: String): Unit = {
    val document = buildJsonDocument(data)
    val future: Future[WriteResult] = ApiHandler.coll.insert(document)
    future.onComplete {
      case Failure(e) =>
        println(s"InsertMongo - NOK: $e")
      case Success(writeResult) =>
        println(s"InsertMongo - OK: $writeResult")
    }
  }

  def buildJsonDocument(data: String): BSONDocument = {
    println(s"Converting\n data $data")
    val dataArray = data.split(",")
    val imei: String = dataArray(0)
    val key: String = dataArray(1)
    val d: String = dataArray(2) + "," + dataArray(3)
    val gpsData = dataArray.toIndexedSeq.drop(4).dropRight(2).mkString(",")

    val document = BSONDocument(
      "imei" -> imei,
      "key" -> key,
      "d" -> d,
      "gps" -> gpsData)

    document
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
