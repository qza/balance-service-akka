package org.qza.bs

import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest

class AppRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest with AppRoutes {

  "The app" should {

    "return index.html on a GET to /" in {
      HttpRequest() ~> routes ~> check {
        status.isSuccess() shouldEqual true
      }
    }

    "return 404 on a GET to /balances" in {
      HttpRequest(uri = "/balances") -> routes -> check {
        status.equals(StatusCodes.NotFound) shouldEqual true
      }
    }

    "return 404 on a GET to /health" in {
      HttpRequest(uri = "/health") -> routes -> check {
        status.equals(StatusCodes.NotFound) shouldEqual true
      }
    }

  }

}
