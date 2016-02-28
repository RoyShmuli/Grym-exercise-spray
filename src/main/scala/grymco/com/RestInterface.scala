package grymco.com

import akka.actor._
import akka.util.Timeout
import spray.http.StatusCodes
import spray.httpx.SprayJsonSupport._
import spray.routing._

import scala.concurrent.duration._
import scala.language.postfixOps

class RestInterface extends HttpServiceActor
  with RestApi {

  def receive = runRoute(routes)
}

trait RestApi extends HttpService with ActorLogging { actor: Actor =>
  implicit val timeout = Timeout(10 seconds)

  def routes: Route =
    
    pathPrefix("simple") {
      pathEnd {
        get {
          parameter("name") { name =>
            complete(name)
          }
        }
      }
    }
}

