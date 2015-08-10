name := "OpenTrackerServer"

version := "1.0"

scalaVersion := "2.11.6"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.12",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.12",
  "com.typesafe" % "config" % "1.3.0",
  "io.spray" % "spray-can_2.11" % "1.3.2",
  "io.spray" % "spray-http_2.11" % "1.3.2",
  "io.spray" % "spray-client_2.11" % "1.3.2",
  "io.spray" % "spray-util_2.11" % "1.3.2"
  )
 
