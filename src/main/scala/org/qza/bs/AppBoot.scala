package org.qza.bs

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging
import scala.concurrent.ExecutionContext

trait AppBoot extends AppCore with AppRoutes with StrictLogging {

  implicit val system = actorSystem
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val appName = config.getString("app.name")
  val httpPort = config.getInt("http.port")
  val httpHost = config.getString("http.host")

  logger.info(s"starting application $appName")

  val bindingFuture = Http().bindAndHandle(
    handler = routes, interface = httpHost, port = httpPort
  )

  bindingFuture onSuccess {
    case _ =>
      logger.info(s"server up and running on $httpHost:$httpPort")
  }

  bindingFuture onFailure {
    case ex: Exception =>
      logger.error(s"server bind to $httpHost:$httpPort failed", ex)
  }

}