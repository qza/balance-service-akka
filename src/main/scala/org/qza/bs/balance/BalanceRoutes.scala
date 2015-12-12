package org.qza.bs.balance

import akka.actor.Props
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import org.qza.bs.AppCore
import org.qza.bs.balance.BalanceModel._

import spray.json._

trait BalanceRoutes extends AppCore with BalanceData with BalanceJsonProtocol {

  val service = actorSystem.actorOf(Props[BalanceService], "service-actor")

  val balanceRoutes = {
    path("balances") {
      get {
        complete(ToResponseMarshallable(data))
      }
    } ~ path("balances" / Segment / "total") { (name) =>
      parameters('callback) { (callbackUrl) =>
        get {
          logger.info(s"received balance total request for ${name}")
          service ! BalanceTotalRequest(name, callbackUrl)
          complete(OK)
        }
      }
    }
  }

  val balanceExternalRoutes = {
    path("ext" / IntNumber / "name" / Segment) { (sourceId, name) =>
      get {
        Thread.sleep(Math.random() * 5000 toInt)
        val balance = data.filter(el => el.name.equals(name)).foldLeft(0L)((sum, el) => sum + el.balance)
        complete(ToResponseMarshallable(BalanceResponse(name, balance)))
      }
    } ~ path("ext" / "callback" / "balance" / "total") {
      post {
        decodeRequest {
          entity(as[BalanceTotalResponse]) { totalResponse =>
            logger.info("received balance total callback: " + totalResponse.toJson.compactPrint)
            complete(OK)
          }
        }
      }
    }
  }

}