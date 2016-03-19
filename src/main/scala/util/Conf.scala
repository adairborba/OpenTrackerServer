package util

import akka.actor.{ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import com.typesafe.config.{Config, ConfigFactory}

class ConfExtensionImpl(config: Config) extends Extension {
  config.checkValid(ConfigFactory.defaultReference)

  val apiUrl = config.getString("open-tracker.api.url")

  val interface = config.getString("open-tracker.app.interface")
  val appPort = config.getInt("open-tracker.app.tcpPort")
  val httpPort = config.getInt("open-tracker.app.httpPort")

  val dbServer = config.getString("open-tracker.db.server")
  val dbName = config.getString("open-tracker.db.name")
  val dbPort = config.getString("open-tracker.db.port")

  val mqttBroker = config.getString("open-tracker.mqtt.broker")
  val key = config.getString("open-tracker.mqtt.key")
}

object ConfExtension extends ExtensionId[ConfExtensionImpl] with ExtensionIdProvider {
  def lookup() = ConfExtension

  def createExtension(system: ExtendedActorSystem) = new ConfExtensionImpl(system.settings.config)
}
