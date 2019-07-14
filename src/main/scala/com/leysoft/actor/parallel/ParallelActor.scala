package com.leysoft.actor.parallel

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.Config

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.util.{Failure, Success}

class ParallelActor(val config: Config) extends Actor with ActorLogging {

  implicit val timeout = Timeout(5 seconds)

  val sumActor: ActorRef = context.actorOf(OperationActor.props,
    config.getString("system.actor.parallel.sum"))

  val susbtActor: ActorRef = context.actorOf(OperationActor.props,
    config.getString("system.actor.parallel.susbtration"))

  override def receive: Receive = {
    case (a: Int, b: Int) =>
      val sum: Future[Int] = (sumActor ? Sum(a, b)).mapTo[Int]
      val subtraction: Future[Int] = (susbtActor ? Subtraction(a, b)).mapTo[Int]
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

  def apply(config: Config): ParallelActor = new ParallelActor(config)

  def props(config: Config) = Props(ParallelActor(config))
}