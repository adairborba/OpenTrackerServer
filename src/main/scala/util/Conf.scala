package util

import com.typesafe.config.{Config, ConfigFactory}
import akka.actor.{ExtendedActorSystem, ExtensionIdProvider, ExtensionId, Extension}

class ConfExtensionImpl(config: Config) extends Extension {
  config.checkValid(ConfigFactory.defaultReference)

  val appHostName = config.getString("tcp-async.app.hostname")
  val appPort = config.getInt("tcp-async.app.port")

  val apiUrl = config.getString("tcp-async.api.url")
}

object ConfExtension extends ExtensionId[ConfExtensionImpl] with ExtensionIdProvider {
  def lookup() = ConfExtension

  def createExtension(system: ExtendedActorSystem) = new ConfExtensionImpl(system.settings.config)
}
