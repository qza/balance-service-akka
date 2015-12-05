package org.qza.bs

import akka.actor.ActorSystem

trait Core {

  protected implicit def actorSystem: ActorSystem

}

trait AppCore extends Core with AppConfig {

  def actorSystem: ActorSystem = ActorSystem("balance-actor-system")

  sys.addShutdownHook(actorSystem.shutdown())

}
