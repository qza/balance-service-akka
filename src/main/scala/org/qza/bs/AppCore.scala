package org.qza.bs

import akka.actor.ActorSystem

trait Core {

  protected implicit def system: ActorSystem

}

trait AppCore extends Core with AppConfig {

  val appName  = config.getString("app.name")

  val httpPort = config.getString("http.port")
  val httpHost = config.getString("http.host")

  def system: ActorSystem = ActorSystem("balance-actor-system")

  sys.addShutdownHook(system.shutdown())

}
