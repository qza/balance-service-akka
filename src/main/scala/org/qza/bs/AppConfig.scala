package org.qza.bs

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.StrictLogging

trait AppConfig {

  def config = AppConfig.config

}

object AppConfig extends StrictLogging {

  logger.info("loading configuration ...")

  val configPath = System.getProperty("config", "src/main/resources/application.conf")

  val config: Config = ConfigFactory.load(ConfigFactory.parseFileAnySyntax(new File(configPath)))

}