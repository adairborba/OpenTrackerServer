package storage

import akka.actor.ActorSystem
import api.{Api, MongoApi}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}

/**
  * Created by drashko on 16.12.15.
  */


@RunWith(classOf[JUnitRunner])
class DatabaseTest extends FunSuite with BeforeAndAfter {

  before {
    val system = ActorSystem("server")
    MongoApi.initDb

    println("DB Started: " + MongoApi.getStatus())
  }
  after {
    println("Before Stop:" + MongoApi.getStatus())
  }

  test("Test Insert Document") {
    val data: String = "865733021674619,XXXXXXXX,221015,21315800,50.014862,14.427030,0.00,242.10,249.02,80,14,12.75,0"

    1 to 10000 foreach { _ => MongoApi.insertDataIntoDB(Api.getCurrentDate(), data) }

    MongoApi.printCollection("XXXXXXXXXXX")
    Thread.sleep(2000)
  }
}
