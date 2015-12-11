package org.qza.bs.balance

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import spray.json.DefaultJsonProtocol

import com.typesafe.scalalogging.StrictLogging

object BalanceModel {

  trait BalanceData extends StrictLogging {
    val data = List(BalanceResponse("mark", 100L), BalanceResponse("tom", 1000L))
  }

  case class BalanceRequest(name: String) {
    override def toString() = {
      s"balance request ${name}"
    }
  }

  case class BalanceResponse(name: String, balance: Long) {
    override def toString() = {
      s"balance response ${name}, balance: ${balance}"
    }
  }

  case class BalanceTotalRequest(name: String, callbackUrl: String) {
    override def toString() = {
      s"balance total request for: ${name}"
    }
  }

  case class BalanceTotalResponse(request: BalanceTotalRequest, total: Long) {
    override def toString() = {
      s"balance total response for: ${request.name}, total amount: ${total}"
    }
  }

  trait BalanceJsonProtocol extends BalanceResponseJson with BalanceTotalRequestJson with BalanceTotalResponseJson

  trait BalanceResponseJson extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val balanceResponseJson = jsonFormat2(BalanceResponse)
  }

  trait BalanceTotalRequestJson extends SprayJsonSupport with DefaultJsonProtocol {
    implicit val balanceTotalRequestJson = jsonFormat2(BalanceTotalRequest)
  }

  trait BalanceTotalResponseJson extends SprayJsonSupport with BalanceTotalRequestJson with DefaultJsonProtocol {
    implicit val balanceTotalResponseJson = jsonFormat2(BalanceTotalResponse)
  }

}

