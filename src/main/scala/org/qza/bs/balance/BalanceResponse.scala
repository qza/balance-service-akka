package org.qza.bs.balance

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

case class BalanceResponse(name: String, balance: Long)

trait BalanceResponseJson extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val balanceResponseJson = jsonFormat2(BalanceResponse)
}

trait BalanceData {
  val data = List(BalanceResponse("mark", 100L), BalanceResponse("tom", 1000L))
}