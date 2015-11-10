package api

import java.text.SimpleDateFormat

import akka.actor.ActorSystem
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.eclipse.paho.client.mqttv3.{MqttClient, MqttConnectOptions, MqttMessage}
import spray.json._
import util.ConfExtension


/**
  * Created by drashko on 23.10.15.
  */
object MqttApi {

  val system = ActorSystem("server")

  val mqttBroker = ConfExtension(system).mqttBroker
  println("Using MQTT broker at " + mqttBroker)

  def prepareMessage(now:String, data: String): String = {

    val dataArray = data.split(",")

    //0 "865733021674619,
    //1 XXXXXXXX,
    //2 221015,
    //3 21315800,
    //4 50.014862,
    //5 14.427030,
    //6 0.00,
    //7 242.10,
    //8 249.02,
    //9 80,14,
    //10 12.75,0"

    //TODO: Compare EPOC from device -> dataArray(2) + dataArray(3) vs $now

    val time = getEpocTime(now)
    val lat = dataArray(4)
    val lon = dataArray(5)
    val bat = dataArray(10)

    val msg =
      s"""{
      "_type" : "location",
      "alt" : "0",
      "cog" : "-1",
      "lat" : $lat,
      "lon" : $lon,
      "t"   : "p",
      "tid" : "VW",
      "batt": $bat,
      "tst" : $time,
      "vac" : "-1",
      "vel" : "-1"
      }"""
    msg.parseJson.compactPrint
  }

  def getEpocTime(date: String): Long = {
    new SimpleDateFormat("ddMMyyHHmmssSS").parse(date).getTime / 1000
  }

  def sendData(content: String, topic: String): Unit = {
    val qos = 0;
    val clientId = "OpenTracker";
    val persistence = new MemoryPersistence();

    try {
      val sampleClient = new MqttClient(mqttBroker, clientId, persistence);
      val connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);
      sampleClient.connect(connOpts);

      val message = new MqttMessage(content.getBytes());
      message.setQos(qos);
      message.setRetained(true);
      sampleClient.publish(topic, message);
      sampleClient.disconnect();
    } catch {
      case e: Exception =>
        e.printStackTrace();
    }
  }
}
