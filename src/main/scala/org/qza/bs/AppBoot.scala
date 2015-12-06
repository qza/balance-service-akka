package org.qza.bs

import scala.io.Source
import scala.concurrent.ExecutionContext

import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.StrictLogging

trait AppBoot extends AppCore with AppRoutes with StrictLogging {

  implicit val system = actorSystem
  implicit val executor: ExecutionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val appName = config.getString("app.name")
  val appBanner = config.getString("app.banner")
  val httpPort = config.getInt("http.port")
  val httpHost = config.getString("http.host")

  logger.info(s"starting application $appName")

  val bindingFuture = Http().bindAndHandle(
    handler = routes, interface = httpHost, port = httpPort
  )

}