package org.qza.bs.balance

import org.scalatest.{Matchers, WordSpec}
import org.qza.bs.balance.BalanceModel._
import spray.json._

class BalanceModelSpec extends WordSpec with Matchers with BalanceJsonProtocol {

  val balanceTotalRequest = """{"name":"mark","callbackUrl":"http://localhost:8080/callback"}"""

  val balanceTotalResponse = """{"request":{"name":"mark","callbackUrl":"http://localhost:8080/callback"},"total":300}"""

  val balanceTotalRequestModel = BalanceTotalRequest("mark", "http://localhost:8080/callback")

  val balanceTotalResponseModel = BalanceTotalResponse(balanceTotalRequestModel, 300L)

  "The App protocols" should {

    "convert balanceTotalRequest json to model" in {
      balanceTotalRequestModel.toJson.toString() shouldBe balanceTotalRequest
    }

    "convert balanceTotalRequestModel to json" in {
      balanceTotalRequest.parseJson.convertTo[BalanceTotalRequest] shouldBe balanceTotalRequestModel
    }

    "convert balanceTotalResponse json to model" in {
      balanceTotalResponseModel.toJson.toString() shouldBe balanceTotalResponse
    }

    "convert balanceTotalResponseModel to json" in {
      balanceTotalResponse.parseJson.convertTo[BalanceTotalResponse] shouldBe balanceTotalResponseModel
    }

  }

}
