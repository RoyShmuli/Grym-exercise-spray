package scala.route


import grymco.com.RestApi
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest

/**
  * Created by Roy on 29/02/2016.
  */
class RouteSpec extends Specification with Specs2RouteTest with RestApi {
  def actorRefFactory = system

  "The service" should {
    "return a 'roy' response for GET requests to /simple?name=roy" in {
      Get("/simple?name=roy") ~> routes ~> check {
        responseAs[String] === "roy"
      }
    }

    "return an error response for GET requests because missing the parameter name: /simple?any=roy" in {
      Get("/simple?any=roy") ~> routes ~> check {
        handled ===  false
      }
    }

    "return an error response for GET requests that doesn't exits: /does/not/exists/" in {
      Get("/does/not/exists/") ~> routes ~> check {
        handled ===  false
      }
    }
  }
}