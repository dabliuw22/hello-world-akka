package com.leysoft.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

class ParallelActor extends Actor with ActorLogging {

  implicit val timeout = Timeout(5 seconds)

  val sumActor: ActorRef = context.actorOf(OperationActor.props, "sum-actor")

  val susbtrationActor: ActorRef = context.actorOf(OperationActor.props, "susbtration-actor")

  override def receive: Receive = {
    case (a: Int, b: Int) =>
      val sum: Future[Int] = (sumActor ? Sum(a, b)).mapTo[Int]
      val subtraction: Future[Int] = (susbtrationActor ? Subtraction(a, b)).mapTo[Int]
      val future = for {
        x <- sum
        y <- subtraction
      } yield (x + y)
      future onComplete  {
        case Success(result) => log.info("Parallel Result: {}", result)
        case Failure(fail) => log.info("Parallel Error: ", fail)
      }
  }
}

object ParallelActor {

  def apply: ParallelActor = new ParallelActor()

  def props = Props[ParallelActor]
}