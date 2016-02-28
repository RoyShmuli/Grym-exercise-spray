package grymco.com

import akka.actor._
import akka.event.Logging
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import grymco.com.config.SprayConfig
import spray.can.Http

import scala.concurrent.duration._

object Main extends App {
  SprayConfig.init()

  implicit val system = ActorSystem("Spray")
  val log = Logging.getLogger(system, this)

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
        system.terminate()
    }
}
