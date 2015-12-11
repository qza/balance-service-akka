package org.qza.bs.balance

import akka.actor.SupervisorStrategy.{Stop, Restart}
import akka.actor._
import akka.event.LoggingReceive
import akka.http.scaladsl.Http

import akka.http.scaladsl.model.{HttpRequest, StatusCodes, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.routing.SmallestMailboxPool
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.ImplicitMaterializer
import org.qza.bs.{AppCore, AppConfig}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Await, ExecutionContext, TimeoutException}
import scala.concurrent.duration._

import org.qza.bs.balance.BalanceModel._


case class BalanceInfoRequest(request: BalanceTotalRequest, bank: Integer)

case class BalanceInfoException(message: String) extends Throwable

class BalanceCollector(caller: ActorRef) extends Actor with AppCore with ActorLogging {

  implicit val system = actorSystem
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val banks = List(111, 222, 333)
  val router = SmallestMailboxPool(banks.size)

  val balanceCollectorActor = system.actorOf(
    Props(new BalanceCollectorActor(banks, self, executor)).withRouter(router), name = "balanceCollector"
  )

  override def receive = {
    case request: BalanceTotalRequest =>
      balanceCollectorActor ! request
    case response: BalanceTotalResponse =>
      caller ! response
    case unexpected: Any =>
      log.debug("unexpected message: {}", unexpected)
      throw new Exception("unexpected message %s".format(unexpected))
  }
}

class BalanceCollectorActor(bankIds: List[Int], originalSender: ActorRef, ec: ExecutionContext) extends Actor with AppCore with ActorLogging {

  implicit val executor = ec

  val router = SmallestMailboxPool(nrOfInstances = 3)

  val workerActor = context.actorOf(Props(new BalanceCollectorWorkerActor).withRouter(router))

  val responses = ArrayBuffer.empty[BalanceTotalResponse]

  override def receive = LoggingReceive {
    case request: BalanceTotalRequest =>
      bankIds.map((bank) => {
        workerActor ! new BalanceInfoRequest(request, bank)
      })
    case response: BalanceTotalResponse =>
      responses += response
      collect(response)
    case unexpected: Any =>
      log.debug("unexpected message: {}", unexpected)
      throw new Exception("unexpected message %s".format(unexpected))
  }

  def collect(response: BalanceTotalResponse) = {
    if (responses.toList.size == bankIds.size) {
      val total = responses.toList.foldLeft(0L)((sum, el) => sum + el.total)
      respondAndShutdown(BalanceTotalResponse(response.request, total))
    }
  }

  def respondAndShutdown(response: Any) = {
    originalSender ! response
    log.debug(s"exiting")
    context.stop(self)
  }

  override val supervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 5) {
      case exception: BalanceInfoException =>
        log.warning("retrying after bc exception : {}", exception.getMessage)
        Restart
      case timeout: TimeoutException =>
        log.warning("retrying after timeout exception : {}", timeout)
        Restart
      case unknown: Exception =>
        log.error("terminating call after unknown exception : {}", unknown)
        Stop
      case unexpected: Any =>
        log.error("terminating call after unexpected exception: {}", unexpected)
        Stop
    }
}

class BalanceCollectorWorkerActor extends Actor with AppConfig with BalanceJsonProtocol with ImplicitMaterializer with ActorLogging {

  import context.dispatcher
  import akka.pattern.pipe

  val http = Http(context.system)
  val httpHost = config.getString("http.host")
  val httpPort = config.getInt("http.port")

  override def receive = {
    case message: BalanceInfoRequest => {
      val ogSender = sender()
      val requestUri = s"http://${httpHost}:${httpPort}/ext/${message.bank}/name/${message.request.name}"
      Await.result(http.singleRequest(HttpRequest(uri = requestUri)), 60 seconds) match {
        case HttpResponse(StatusCodes.OK, headers, entity, _) =>
          Unmarshal(entity).to[BalanceResponse].map((el) => BalanceTotalResponse(message.request, el.balance)).pipeTo(ogSender)
        case HttpResponse(code, _, _, _) =>
          throw new BalanceInfoException(s"request failed, response code: ${code}")
      }
    }
  }

}