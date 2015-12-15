package api

import akka.actor.ActorSystem
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument
import util.{ConfExtension, DateTimeUtil}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}


/**
  * Created by drashko on 22.10.15.
  */
object MongoApi {

  val system = ActorSystem("server")

  val dbServer = ConfExtension(system).dbServer
  val dbPort = ConfExtension(system).dbPort
  val dbName = ConfExtension(system).dbName

  println("Starting DB connection [" + dbServer + ":" + dbPort + "/" + dbName + "{points}] ...")

  val driver = new MongoDriver
  val moncon = driver.connection(List(dbServer + ":" + dbPort))

  // Gets a reference to the database "plugin"
  val db = moncon(dbName)
  val coll = db[BSONCollection]("points")
  coll.create().map(c =>
    println(getStatus)
  )

  def insertDocument(document: BSONDocument): Unit = {
    val future: Future[WriteResult] = coll.insert(document)
    future.onComplete {
      case Failure(e) =>
        println(s"InsertMongo - NOK: $e")
      case Success(writeResult) =>
        println(s"InsertMongo - OK: $writeResult")
    }
  }

  def getStatus(): String = {
    val result = coll.stats()
    val status = result map { csr =>
      Map(
        "Time" -> DateTimeUtil.getCurrentDateTimeAsString,
        "Count=" -> csr.count,
        "StorageSize (mb)=" -> (csr.storageSize / 1048576).toInt,
        "Size (mb)=" -> (csr.size / 1048576).toInt,
        "Avarage Object Size (bytes)=" -> csr.averageObjectSize.map(_.toInt).getOrElse(0),
        "Total Index Size (kb)=" -> csr.totalIndexSize / 1024)
    }
    Await.result(status, 4 second)
    status.value.mkString("\n")
  }

  def insertDataIntoDB(now:String, data: String): Unit = {
    val document = buildJsonDocument(now, data)
    MongoApi.insertDocument(document)

  }

  def buildJsonDocument(now:String, data: String): BSONDocument = {
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
