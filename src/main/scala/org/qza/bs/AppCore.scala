package org.qza.bs

import akka.actor.ActorSystem

trait Core {

  protected implicit def actorSystem: ActorSystem

}

trait AppCore extends Core with AppConfig {

  val httpPort = config.getInt("http.port")

  val httpHost = config.getString("http.host")

  def actorSystem: ActorSystem = ActorSystem("balance-actor-system")

  sys.addShutdownHook(actorSystem.shutdown())

}
