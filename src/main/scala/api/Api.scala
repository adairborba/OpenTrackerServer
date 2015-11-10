package api

import java.text.SimpleDateFormat
import java.util.{Date, Calendar}

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

  def getCurrentDate(now:Date = Calendar.getInstance().getTime()) = {
    new SimpleDateFormat("ddMMyyHHmmssSS").format(now)
  }

  /**
    * data 865733021674619,XXXXXXXXXXX,                    221015,13144200,50.012703,14.427660,0.00,258.90,342.66,64,19,14.65,1
    * data 865733021674619,XXXXXXXXXXX,10/22/15,23:21:39+0,100815,23214000,50.012516,14.427183,0.00,256.80,80.43,59,18,19.61,1
    *
    * Build HTTP string from RAW comma separated data
    * @param data
    *
    **/
  def buildHttpString(now: String, data: String): String = {
    println(s"Converting\n data $data | $now")
    val dataArray = data.split(",")
    val imei: String = dataArray(0)
    val key: String = dataArray(1)
    val d: String = mkDate(dataArray(2)) + "," + mkTime(dataArray(3))
    val gpsData = dataArray.toIndexedSeq.drop(2).dropRight(2).mkString(",")
    val httpData = s"imei=$imei&key=$key&d=$d[$gpsData]{}"
    httpData
  }

  def mkDate(date: String): String = {
    val day = date.substring(0, 2)
    val mon = date.substring(2, 4)
    val yrr = date.substring(4, 6)
    s"$day/$mon/$yrr"
  }

  def mkTime(time: String): String = {
    val hh = time.substring(0, 2)
    val mm = time.substring(2, 4)
    val ss = time.substring(4, 6)
    s"$hh:$mm:$ss+0"
  }
}
