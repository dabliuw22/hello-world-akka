package com.leysoft.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

import scala.collection.immutable.List

class CheckerUserActor(actor: ActorRef) extends Actor with ActorLogging {

  val usernames = List("username1", "username2", "username3")

  val function: ((List[String], User) => Boolean) =
    (names, user) => names.contains(user.username)

  override def receive: Receive = {
    case user: User if function(usernames, user) => log.info("Success")
      actor ! user
      sender() ! true
    case _ => log.info("Failure")
      sender() ! false
  }

  override def preStart: Unit = log.info("START")

  override def postStop: Unit = log.info("STOP")
}

object CheckerUserActor {

  def apply(actor: ActorRef): CheckerUserActor = new CheckerUserActor(actor)

  def props(actor: ActorRef) = Props(CheckerUserActor(actor))
}
