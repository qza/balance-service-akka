package org.qza.bs.transaction

import java.util.{Random, Date}
import akka.actor.{ActorLogging, Actor}
import org.qza.bs.AppCore
import kafka.producer.{ProducerConfig, Producer, KeyedMessage}

sealed trait Type

case class Take() extends Type

case class Place() extends Type

case class Transaction(id: Long, name: String, typee: Type, amount: Long)

case class GenerateLog(count: Integer)

class TransactionLogProducer extends Actor with AppCore with ActorLogging {

  val random = new Random()
  val props = new java.util.Properties()

  val names = Array("mark", "tom", "john")
  val actions = Array(Take, Place)

  props.put("bootstrap.servers", "localhost:9092")
  props.put("client.id", "KafkaProducer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

  val producerConfig = new ProducerConfig(props)
  val producer = new Producer[AnyRef, AnyRef](producerConfig)

  def receive = {
    case action: GenerateLog =>
      val startTime = new Date().getTime
      for (index <- Range(0, action.count)) {
        val timestamp = new Date().getTime
        val amount = random.nextInt(1001)
        val action = actions(random.nextInt(2))
        val name = names(random.nextInt(2))
        val message = s"$name,$action,$amount,$timestamp"
        producer.send(new KeyedMessage("transactions", name, message))
      }
      val endTime = new Date().getTime
      log.info(s"sent ${action.count} messages in ${(endTime-startTime)/1000} seconds")
      producer.close()
    case _ => log.warning("unknown message")
  }

}
