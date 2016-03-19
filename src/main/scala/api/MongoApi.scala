package api

import akka.actor.ActorSystem
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONObjectID}
import util.ConfExtension

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}


case class Point(id: BSONObjectID, timestamp: String, imei: String, key: String, d: String, gps: String)


object Point {

  implicit object PersonReader extends BSONDocumentReader[Point] {
    def read(doc: BSONDocument): Point = {
      val id = doc.getAs[BSONObjectID]("_id").get
      val timestamp = doc.getAs[String]("timestamp").get
      val imei = doc.getAs[String]("imei").get
      val key = doc.getAs[String]("key").get
      val d = doc.getAs[String]("d").get
      val gps = doc.getAs[String]("gps").get

      Point(id, timestamp, imei, key, d, gps)
    }
  }

}

/**
  * Created by drashko on 22.10.15.
  */
object MongoApi {

  val system = ActorSystem("server")

  val dbServer = ConfExtension(system).dbServer
  val dbPort = ConfExtension(system).dbPort
  val dbName = ConfExtension(system).dbName

  val driver = new MongoDriver

  // #servers:
  //val connectionSTr = "10.211.55.5:27017,10.211.55.11:27017,10.211.55.4:27017"
  val connectionSTr = dbServer + ":" + dbPort
  val serverList = connectionSTr.split(",")

  //val moncon = driver.connection(serverList)
  val moncon = driver.connection(List(dbServer + ":" + dbPort))

  println("Starting DB connection [" + serverList.mkString(",") + "]/" + dbName + " ...")
  println("")


  // Gets a reference to the database "plugin"
  val db = moncon(dbName)
  val coll = db[BSONCollection]("points")

  def initDb() = {
    val res = coll.create().map(c =>
      println("Collection created")
    )
    Await.result(res, 10 second)
  }

  def printCollection(key: String) = {
    coll.
      find(BSONDocument("key" -> key)).
      cursor[Point]().
      collect[List]().
      map { list =>
        for (point <- list) println(s"found $point")
      }.onComplete {
      case Failure(e) =>
        println(s"printCollection - NOK: $e")
      case Success(writeResult) =>
        println(s"printCollection - OK: $writeResult")
    }
  }

  def getStatus(): String = {
    val result = coll.stats()
    val status = result map { csr =>
      Map(
        "Time" -> Api.getCurrentDate(),
        "Count=" -> csr.count,
        "StorageSize (mb)=" -> (csr.storageSize / 1048576).toInt,
        "Size (mb)=" -> (csr.size / 1048576).toInt,
        "Avarage Object Size (bytes)=" -> csr.averageObjectSize.map(_.toInt).getOrElse(0),
        "Total Index Size (kb)=" -> csr.totalIndexSize / 1024)
    }
    Await.result(status, 10 second)
    status.value.mkString("\n")
  }

  def insertDataIntoDB(now: String, data: String): Future[WriteResult] = {
    val document = buildJsonDocument(now, data)
    insertDocument(document)
  }

  def insertDocument(document: BSONDocument): Future[WriteResult] = {
    val future: Future[WriteResult] = coll.insert(document)
    future.onComplete {
      case Failure(e) =>
        println(s"InsertMongo - NOK: $e")
      case Success(writeResult) =>
        println(s"InsertMongo - OK: $writeResult")
    }
    future
  }

  def buildJsonDocument(now: String, data: String): BSONDocument = {
    println(s"Converting\n data $data")
    val dataArray = data.split(",")
    val imei: String = dataArray(0)
    val key: String = dataArray(1)
    val d: String = dataArray(2) + "," + dataArray(3)
    val gpsData = dataArray.toIndexedSeq.drop(4).dropRight(2).mkString(",")

    val document = BSONDocument(
      "timestamp" -> now,
      "imei" -> imei,
      "key" -> key,
      "d" -> d,
      "gps" -> gpsData)

    document
  }
}
