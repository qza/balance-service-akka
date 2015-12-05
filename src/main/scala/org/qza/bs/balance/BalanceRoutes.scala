package org.qza.bs.balance

import akka.event.Logging._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._

trait BalanceRoutes {

  val balanceRoutes = logRequestResult("balance-service", InfoLevel) {
    path("balances") {
      get {
        complete(StatusCodes.NoContent)
      }
    }
  }

}