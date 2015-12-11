package org.qza.bs.balance

import akka.actor.{Props, ActorLogging, Actor}
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.util.{ByteString}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Await}

import org.qza.bs.AppCore
import org.qza.bs.balance.BalanceModel._

class BalanceService extends Actor with AppCore with BalanceJsonProtocol with ActorLogging {

  val http = Http(context.system)

  implicit val materializer = ActorMaterializer()
  implicit val executor: ExecutionContext = actorSystem.dispatcher

  override def receive = {
    case request: BalanceTotalRequest =>
      log.debug(s"received balance total request")
      val collectorActor = actorSystem.actorOf(Props(new BalanceCollector(self)), "collector-actor")
      collectorActor ! request
    case response: BalanceTotalResponse =>
      val callbackUrl = response.request.callbackUrl
      val callbackRequest = RequestBuilding.Post[BalanceTotalResponse](uri = callbackUrl, content = response)
      Await.result(http.singleRequest(callbackRequest), 5 seconds) match {
        case HttpResponse(StatusCodes.OK, headers, _, _) =>
          log.info(s"callback success: ${callbackUrl}")
        case HttpResponse(code, _, _, _) =>
          log.error(s"callback failed, response code: ${code}")
    }
  }

}
