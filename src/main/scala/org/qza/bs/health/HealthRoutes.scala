package org.qza.bs.health

import akka.event.Logging._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._

trait HealthRoutes {

  val healthRoutes = logRequestResult("balance-service-health", InfoLevel) {
    path("health") {
      get {
        complete(StatusCodes.NotFound)
      }
    }
  }

}
