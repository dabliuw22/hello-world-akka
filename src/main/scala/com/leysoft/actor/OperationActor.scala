package com.leysoft.actor

import akka.actor.{Actor, ActorLogging, Props}

class OperationActor extends Actor with ActorLogging {

  override def receive: Receive = {
    case Sum(a, b) => sender() ! a + b
    case Subtraction(a, b) => sender() ! a - b
    case _ => log.error("Error")
      sender() ! 0
  }
}

object OperationActor {

  def apply: OperationActor = new OperationActor()

  def props = Props[OperationActor]
}

case class Subtraction(a: Int, b: Int)
case class Sum(a: Int, b: Int)