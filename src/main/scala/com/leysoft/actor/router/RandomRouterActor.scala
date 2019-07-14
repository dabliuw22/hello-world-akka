package com.leysoft.actor.router

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.util.Random

class RandomRouterActor extends Actor with ActorLogging {

  val routees: List[ActorRef] = List.fill(5)(context.actorOf(Props[WorkerActor]))

  override def receive: Receive = {
    case message: Work =>
      log.info("Router")
      routees(Random.nextInt(routees.size)) forward message
    case _ => log.info("Invalid")
  }
}

object RandomRouterActor {

  def apply: RandomRouterActor = new RandomRouterActor()

  def props = Props[RandomRouterActor]
}
