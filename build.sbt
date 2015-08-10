name := "OpenTrackerServer"

version := "1.0"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.2.0",
  "com.typesafe.akka" %% "akka-testkit" % "2.2.0",
  "com.typesafe" % "config" % "1.3.0",
  "com.typesafe.scala-logging" %% "scala-logging-slf4j" % "2.1.2",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
  "io.spray" % "spray-can" % "1.2-20130912",
  "io.spray" % "spray-http" % "1.2-20130912",
  "io.spray" % "spray-httpx" % "1.2-20130912",
  "io.spray" % "spray-util" % "1.2-20130912",
  "io.spray" % "spray-client" % "1.2-20130912",
  "io.spray" % "spray-testkit" % "1.2-20130912",
  "io.spray" % "spray-routing" % "1.2-20130912",
  "org.mockito" % "mockito-all" % "1.9.5" % "test"
)
