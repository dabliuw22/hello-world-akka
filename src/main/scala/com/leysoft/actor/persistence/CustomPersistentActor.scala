package com.leysoft.actor.persistence

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}

class CustomPersistentActor(val id: String) extends PersistentActor with ActorLogging {

  var status: Status = Status()

  def updateStatus(event: Event): Unit = {
    status = status.update(event)
  }

  override def persistenceId: String = id

  override def receiveCommand: Receive = {
    case AddCommand(data) =>
      log.info(s"AddCommand($data)")
      persist(AddEvent(data)){event => updateStatus(event)}
      saveSnapshot(status)
    case RemoveCommand(data) =>
      log.info(s"RemoveCommand($data)")
      persist(RemoveEvent(data))(updateStatus)
      saveSnapshot(status)
  }

  override def receiveRecover: Receive = {
    case event: Event =>  updateStatus(event)
    case SnapshotOffer(_, snapshot: Status) => status = snapshot
  }
}

object CustomPersistentActor {

  def apply(id: String): CustomPersistentActor = new CustomPersistentActor(id)

  def props(id: String): Props = Props(CustomPersistentActor(id))
}

sealed trait Event

case class AddEvent(data: String) extends Event

case class RemoveEvent(data: String) extends Event

sealed trait Command

case class AddCommand(data: String) extends Command

case class RemoveCommand(data: String) extends Command

@SerialVersionUID(value = 1L)
case class Status(datas: List[String] = Nil) {

  def update(event: Event): Status = event match {
    case AddEvent(data) => copy(data::datas)
    case RemoveEvent(data) => copy(datas.filterNot(_.equals(data)))
    case _ => this
  }

  def size: Int = datas.size

  override def toString: String = datas.reverse.toString()
}