package org.qza.bs

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.util.ByteString

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

    "return OK on a GET to /health" in {
      Get("/health") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[HealthResponse].status shouldBe "ok"
      }
    }

    "return OK on a GET to /balances" in {
      Get("/balances") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[List[BalanceResponse]] should not be empty
      }
    }

    "return OK on a GET to /ext/111/name/tom" in {
      Get("/ext/111/name/tom?callback=none") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
      }
    }

    "return OK on a GET to /balances/mark/total" in {
      Get("/balances/mark/total?callback=http://localhost:8080/callback") ~> routes ~> check {
        status shouldEqual StatusCodes.OK
      }
    }

    "return OK on a POST to /ext/callback/balances/total" in {

      val jsonRequest = ByteString("""{"request":{"name":"mark","callbackUrl":"http://localhost:8080/callback"},"total":300}""")

      val postRequest = HttpRequest(
        HttpMethods.POST,
        uri = "/ext/callback/balance/total",
        entity = HttpEntity(MediaTypes.`application/json`, jsonRequest)
      )

      postRequest ~> routes ~> check {
        status.isSuccess() shouldEqual true
      }
    }

  }

}
