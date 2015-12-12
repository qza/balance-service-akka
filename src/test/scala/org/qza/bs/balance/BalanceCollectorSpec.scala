package org.qza.bs.balance

import akka.actor.{ActorLogging, Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.qza.bs.AppCore
import org.qza.bs.balance.BalanceModel.{BalanceTotalResponse, BalanceTotalRequest}
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

import scala.concurrent.duration._

class BalanceCollectorSpec extends TestKit(ActorSystem()) with WordSpecLike with MustMatchers with BeforeAndAfterAll with AppCore with ImplicitSender {

  val banks = List(111, 222, 333)

  val stub = actorSystem.actorOf(Props(new BalanceCollectorProxyStub))
  val sut = actorSystem.actorOf(Props(new BalanceCollector(banks, self, stub)))

  "Balance collector worker actor" should {

    "process total request and send total response" in {
      val request = BalanceTotalRequest("tom", "blah")
      sut ! BalanceTotalRequest("tom", "blah")
      expectMsg(30 seconds, BalanceTotalResponse(request, 0L))
    }
  }

  override def afterAll {
    actorSystem.shutdown()
  }
}

class BalanceCollectorProxyStub extends BalanceCollectorProxy with ActorLogging {

  override def receive = {
    case message: BalanceInfoRequest => sender() ! BalanceTotalResponse(message.request, 0L)
    case x: Any => log.warning(s"unknown message ${x}")
  }
}