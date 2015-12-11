package org.qza.bs

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest

import org.scalatest.{Matchers, WordSpec}

import org.qza.bs.health.HealthResponse
import org.qza.bs.balance.BalanceModel._

class AppRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest with AppRoutes {

  "The app" should {

    "return index.html on a GET to /" in {
      Get() ~> routes ~> check {
        status.isSuccess() shouldEqual true
        responseAs[String] should include regex "balance service"
      }
    }

    "return OK on a GET to /balances" in {
      Get("/balances") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[List[BalanceResponse]] should not be empty
      }
    }

    "return OK on a GET to /health" in {
      Get("/health") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[HealthResponse].status shouldBe "ok"
      }
    }

  }

}
