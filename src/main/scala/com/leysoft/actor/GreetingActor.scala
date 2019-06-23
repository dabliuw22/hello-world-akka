package com.leysoft.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class GreetingActor extends Actor with ActorLogging {

  var status = 0

  override def receive: Receive = {
    case Greet =>
      status += 1
      log.info("Status: {}", status)
    case _ => log.info("None")
  }

  override def preStart: Unit = log.info("START")

  override def postStop: Unit = log.info("STOP")
}

object GreetingActor {

  def apply: GreetingActor = new GreetingActor()
}

case class Greet(message: String = "Default")