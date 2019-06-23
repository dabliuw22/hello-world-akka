package com.leysoft.actor

import akka.actor.{Actor, ActorLogging, Props}

class ChildActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case message => log.info("Message {}", message)
      message match {
        case "RESUME" => throw ResumeException
        case "RESTART" => throw RestartException
        case "STOP" => throw StopException
        case _ => throw new Exception
    }
  }

  override def preStart: Unit = log.info("START")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("PRE-RESTART")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    log.info("POST-RESTART")
    super.postRestart(reason)
  }

  override def postStop: Unit = log.info("STOP")
}

object ChildActor {

  def apply: ChildActor = new ChildActor()

  def props: Props = Props[ChildActor]
}