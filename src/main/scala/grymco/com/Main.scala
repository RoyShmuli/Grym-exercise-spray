package grymco.com

import akka.actor._
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import grymco.com.config.SprayConfig
import spray.can.Http

import scala.concurrent.duration._

/**
  * Created by Roy on 28/02/2016.
  */
object Main extends App {
  // Force init configuration on start or exit on error
  SprayConfig.init()

  implicit val system = ActorSystem("Spray")
  val log = system.log

  val api = system.actorOf(Props(new RestInterface()), "httpInterface")

  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)

  val sprayConfig:SprayConfig = SprayConfig.sprayConfig
  IO(Http).ask(Http.Bind(listener = api, interface = sprayConfig.host, port = sprayConfig.port))
    .mapTo[Http.Event]
    .map {
      case Http.Bound(address) =>
        log.info(s"REST interface bound to $address")
      case Http.CommandFailed(cmd) =>
        log.error("FATAL: REST interface could not bind to " +
          s"${sprayConfig.host}:${sprayConfig.port}, ${cmd.failureMessage}")
        system.shutdown()
    }
}
