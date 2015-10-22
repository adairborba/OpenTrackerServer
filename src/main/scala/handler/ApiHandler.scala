package handler

import akka.actor.{ActorRef, Props}
import akka.io.Tcp.Write
import akka.util.ByteString
import api.{Api, MongoApi}
import reactivemongo.bson.BSONDocument
import spray.http.HttpMethods._
import util.ConfExtension

import scala.concurrent.ExecutionContext.Implicits.global

object ApiHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[ApiHandler], connection)
}

object ApiHandler {
  private val newLine = ByteString("\n")
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
    MongoApi.insertDocument(document)

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
    * data 865733021674619,XXXXXXXXXXX,                    221015,13144200,50.012703,14.427660,0.00,258.90,342.66,64,19,14.65,1
    * data 865733021674619,XXXXXXXXXXX,10/22/15,23:21:39+0,100815,23214000,50.012516,14.427183,0.00,256.80,80.43,59,18,19.61,1
    *
    * Build HTTP string from RAW comma separated data
    * @param data
    *
    **/

  def buildHttpString(data: String): String = {
    println(s"Converting\n data $data")
    val dataArray = data.split(",")
    val imei: String = dataArray(0)
    val key: String = dataArray(1)
    val d: String = mkDate(dataArray(2)) + "," + mkTime(dataArray(3))
    val gpsData = dataArray.toIndexedSeq.drop(3).dropRight(2).mkString(",")
    val httpData = s"imei=$imei&key=$key&d=$d[$gpsData]{}"
    httpData
  }

  def mkDate(date: String): String = {
    val mon = date.substring(3, 5)
    val day = date.substring(0, 2)
    val yrr = date.substring(4, 6)
    s"$mon/$day/$yrr"
  }

  def mkTime(time: String): String = {
    val hh = time.substring(0, 2)
    val mm = time.substring(2, 4)
    val ss = time.substring(4, 6)
    s"$hh:$mm:$ss+0"
  }

  /**
    * Sends given data over connection to client, appending a newline
    * @param response
    */
  def respond(response: String) {
    connection ! Write(ByteString(response) ++ ApiHandler.newLine)
  }
}
