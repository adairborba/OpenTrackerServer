name := "OpenTrackerServer"

version := "1.0"

scalaVersion := "2.11.7"

scalaSource in Compile := baseDirectory.value / "src/main/scala"
scalaSource in Test := baseDirectory.value / "src/test/scala"

resolvers += "spray repo" at "http://repo.spray.io"
resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "Typesafe" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += "pago" at "https://repo.eclipse.org/content/repositories/paho-releases/"


dependencyOverrides ++= Set(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.7.0"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.12",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.12",
  "com.typesafe" % "config" % "1.3.0",
  "io.spray" % "spray-can_2.11" % "1.3.2",
  "io.spray" % "spray-http_2.11" % "1.3.2",
  "io.spray" % "spray-client_2.11" % "1.3.2",
  "io.spray" % "spray-util_2.11" % "1.3.2",
  "io.spray" %%  "spray-json" % "1.3.2",
  "org.reactivemongo" %% "reactivemongo" % "0.11.7",
  "org.eclipse.paho" % "org.eclipse.paho.client.mqttv3" % "1.0.2",
  "org.abstractj.kalium" % "kalium" % "0.4.0"
)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "junit" % "junit" % "4.8.1"
)
