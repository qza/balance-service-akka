package org.qza.bs.health

import akka.event.Logging._
import akka.http.scaladsl.server.Directives._

trait HealthRoutes extends HealthResponseJson {

  val healthRoutes = logRequestResult("balance-service-health", InfoLevel) {
    path("health") {
      get {
        complete(HealthResponse("ok"))
      }
    }
  }

}
