package org.qza.bs

import com.typesafe.scalalogging.StrictLogging

object AppBoot extends App with AppCore with StrictLogging {

  logger.info(s"starting app $appName")

}
