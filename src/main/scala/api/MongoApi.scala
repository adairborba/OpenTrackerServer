package api

import akka.actor.ActorSystem
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.BSONDocument
import util.ConfExtension

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

import reactivemongo.api._
import scala.concurrent.ExecutionContext.Implicits.global


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
  coll.create().map ( c =>
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
        "Count=" -> csr.count,
        "StorageSize (mb)=" -> (csr.storageSize / 1048576).toInt,
        "Size (mb)=" -> (csr.size / 1048576).toInt,
        "Avarage Object Size (bytes)=" -> csr.averageObjectSize.map(_.toInt).getOrElse(0),
        "Total Index Size (kb)=" -> csr.totalIndexSize / 1024)
    }
    Await.result(status, 4 second)
    status.value.mkString("\n")
  }
}
