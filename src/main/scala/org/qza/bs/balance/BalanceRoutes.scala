package org.qza.bs.balance

import akka.event.Logging._
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives._

trait BalanceRoutes extends BalanceData with BalanceResponseJson {

  val balanceRoutes = logRequestResult("balance-service", InfoLevel) {
    path("balances") {
      get {
        complete(ToResponseMarshallable(data))
      }
    }
  }

}