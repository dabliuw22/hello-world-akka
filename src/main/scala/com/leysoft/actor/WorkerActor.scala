package com.leysoft.actor

import akka.actor.{Actor, ActorLogging, Props}

class WorkerActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case work: Work => log.info("WORK: {}, ACTOR: {}", work, self)
    case _ => log.info("INVALID")
  }
}

object WorkerActor {

  def apply: WorkerActor = new WorkerActor()

  def props = Props[WorkerActor]
}

case class Work(name: String, description: String)
