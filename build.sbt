name := "OpenTrackerServer"

version := "1.0"

scalaVersion := "2.11.6"

resolvers += "spray repo" at "http://repo.spray.io"
resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += "Typesafe" at "http://repo.typesafe.com/typesafe/releases/"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.12",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.12",
  "com.typesafe" % "config" % "1.3.0",
  "io.spray" % "spray-can_2.11" % "1.3.2",
  "io.spray" % "spray-http_2.11" % "1.3.2",
  "io.spray" % "spray-client_2.11" % "1.3.2",
  "io.spray" % "spray-util_2.11" % "1.3.2",
  "org.reactivemongo" %% "reactivemongo" % "0.11.6"
  )

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
 
