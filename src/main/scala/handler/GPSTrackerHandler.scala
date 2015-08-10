package handler

import akka.actor._
import akka.util.ByteString
import akka.io.Tcp.Write
import api.Api


object GPSTrackerHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[GPSTrackerHandler], connection)
}

class GPSTrackerHandler(connection: ActorRef) extends Handler(connection) {
  def received(data: String) = {
    println("--->" + data)
    connection ! Write(ByteString("OK" + "\n"))

    ApiHandler


  }
}