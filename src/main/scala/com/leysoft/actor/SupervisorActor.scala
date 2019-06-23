package com.leysoft.actor

import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props, SupervisorStrategy}

import scala.concurrent.duration._
import scala.language.postfixOps

class SupervisorActor extends Actor with ActorLogging {

  /**
    * Cantidad de veces que se puede reiniciar un actor secundario.
    */
  val maximumNumberOfRestarts: Int = 10

  /**
    * Ventana de tiempo dentro de la cual no se debe exceder maxNrOfRetries.
    */
  val windowsTime: Int = 1

  /**
    * Chield actor.
    */
  var child: ActorRef = _

  override def receive: Receive = {
    case message => log.info("Message {}", message)
      child ! message
      //Thread.sleep(100)
  }

  override def preStart(): Unit = {
    child = context.actorOf(ChildActor.props, "child-actor")
    //Thread.sleep(100)
  }

  override val supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = maximumNumberOfRestarts,
      withinTimeRange = windowsTime seconds) {
    case ResumeException => Resume
    case RestartException => Restart
    case StopException => Stop
    case _: Exception => Escalate
  }
}

object SupervisorActor {

  def apply: SupervisorActor = new SupervisorActor()

  def props = Props[SupervisorActor]
}

case object ResumeException extends Exception

case object RestartException extends Exception

case object StopException extends Exception
