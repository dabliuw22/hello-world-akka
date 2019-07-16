package com.leysoft.actor.communication

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext

//import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

class UserActor(actor: ActorRef) extends Actor with ActorLogging {

  implicit val timeout = Timeout(5 seconds)

  implicit val executor = ExecutionContext.global

  override def receive: Receive = {
    case user: User => {
      val future = actor ? user
      val status = future.mapTo[Boolean] onComplete {
        case Success(result) =>
          log.info("Result: {}", result)
          result
        case Failure(fail) =>
          log.info("Failure: {}", fail)
          false
      }
      //val result = Await.result(future.mapTo[Boolean], timeout.duration)
    }
    case _ => log.info("None")
  }

  override def preStart: Unit = log.info("START")

  override def postStop: Unit = log.info("STOP")
}

object UserActor {

  def apply(actor: ActorRef): UserActor = new UserActor(actor)
  
  def props(actor: ActorRef) = Props(UserActor(actor))
}

case class User(username: String, email: String)
