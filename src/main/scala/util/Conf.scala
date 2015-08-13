package util

import com.typesafe.config.{Config, ConfigFactory}
import akka.actor.{ExtendedActorSystem, ExtensionIdProvider, ExtensionId, Extension}

class ConfExtensionImpl(config: Config) extends Extension {
  config.checkValid(ConfigFactory.defaultReference)

  val appHostName = config.getString("open-tracker.app.hostname")
  val appPort = config.getInt("open-tracker.app.port")
  val apiUrl = config.getString("open-tracker.api.url")

  val db = config.getString("open-tracker.db.server")
}

object ConfExtension extends ExtensionId[ConfExtensionImpl] with ExtensionIdProvider {
  def lookup() = ConfExtension

  def createExtension(system: ExtendedActorSystem) = new ConfExtensionImpl(system.settings.config)
}
