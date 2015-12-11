package org.qza.bs

import akka.actor.ActorSystem
import akka.event.Logging._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import org.qza.bs.balance.BalanceRoutes
import org.qza.bs.health.HealthRoutes

trait AppRoutes extends BalanceRoutes with HealthRoutes {

  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer

  val webRoutes = {
    path("") {
      get {
        getFromResource("public/index.html")
      }
    } ~
      path("favicon.ico") {
        get {
          complete(StatusCodes.NotFound)
        }
      }
  }

  val routes = webRoutes ~ balanceRoutes ~ balanceExternalRoutes ~ healthRoutes

}
