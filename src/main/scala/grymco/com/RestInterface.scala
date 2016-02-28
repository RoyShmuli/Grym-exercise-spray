package grymco.com

import akka.util.Timeout
import spray.routing._

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by Roy on 28/02/2016.
  */
class RestInterface extends HttpServiceActor
  with RestApi {

  def receive = runRoute(routes)
}

trait RestApi extends HttpService {
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

