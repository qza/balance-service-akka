package org.qza.bs

import scala.io.{Source, StdIn}

object AppMain extends App with AppBoot {

  bindingFuture onSuccess {
    case _ => {
      logger.info(s"server up and running on $httpHost:$httpPort")
      Source.fromFile(appBanner).getLines().foreach(x => println(x))
      logger.info(s"press any key to terminate")
      StdIn.readLine()
      bindingFuture
        .flatMap(_.unbind()) // trigger unbinding from the port
        .onComplete(_ ⇒ system.shutdown()) // and shutdown when done
      logger.info(s"server stopped")
    }
  }

  bindingFuture onFailure {
    case ex: Exception =>
      logger.error(s"server bind to $httpHost:$httpPort failed", ex)
      system.shutdown()
  }

}