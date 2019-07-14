package com.leysoft.actor.communication

import akka.actor.{Actor, ActorLogging, Props}

class StoreUserActor extends Actor with ActorLogging {

  var users = List.empty[User]

  override def receive: Receive = {
    case user: User => users.appended(user)
      log.info("User: {} saved", user)
    case _ => log.info("It was not possible")
  }

  override def preStart: Unit = log.info("START")

  override def postStop: Unit = log.info("STOP")
}

object StoreUserActor {

  def apply: StoreUserActor = new StoreUserActor()

  def props = Props[StoreUserActor]
}
