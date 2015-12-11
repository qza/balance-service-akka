package org.qza.bs.health

import akka.http.scaladsl.server.Directives._

trait HealthRoutes extends HealthResponseJson {

  val healthRoutes = {
    path("health") {
      get {
        complete(HealthResponse("ok"))
      }
    }
  }

}
