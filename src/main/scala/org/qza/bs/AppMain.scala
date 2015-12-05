package org.qza.bs

import scala.io.StdIn

object AppMain extends App with AppBoot {

  logger.info(s"press any key to terminate")

  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.shutdown()) // and shutdown when done

  logger.info(s"server stopped")

}