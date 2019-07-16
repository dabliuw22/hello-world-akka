package com.leysoft

import akka.actor.ActorSystem
import com.leysoft.actor.communication.{CheckerUserActor, StoreUserActor, User, UserActor}
import com.leysoft.actor.parallel.ParallelActor
import com.leysoft.actor.persistence.{AddCommand, CustomPersistentActor, RemoveCommand}
import com.leysoft.actor.router.{RandomRouterActor, Work}
import com.leysoft.actor.supervisor.SupervisorActor
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object Main extends App {
  val config: Config = ConfigFactory.load("application.conf")
  val actorSystem = ActorSystem(config.getString("system.name"))

  val storeUserActor = actorSystem.actorOf(StoreUserActor.props,
    config.getString("system.actor.communication.store"))
  val checkerUserActor = actorSystem.actorOf(CheckerUserActor.props(storeUserActor),
    config.getString("system.actor.communication.checker"))
  val userActor = actorSystem.actorOf(UserActor.props(checkerUserActor),
    config.getString("system.actor.communication.user"))
  userActor ! User("username1", "username1@mail.com")

  val supervisorActor = actorSystem.actorOf(SupervisorActor.props(config),
    config.getString("system.actor.supervisor.supervisor"))
  supervisorActor ! "STOP"

  val randomRouterActor = actorSystem.actorOf(RandomRouterActor.props)
  randomRouterActor ! Work("Name1", "Description1")

  val parallelActor = actorSystem.actorOf(ParallelActor.props(config),
    config.getString("system.actor.parallel.parallel"))
  parallelActor ! (1, 2)

  val persistentActor = actorSystem.actorOf(CustomPersistentActor.props("persintence-id-3"),
    config.getString("system.actor.persistence"))
  persistentActor ! AddCommand("data1")
  persistentActor ! AddCommand("data2")
  persistentActor ! AddCommand("data3")
  persistentActor ! RemoveCommand("data2")

  Thread.sleep(5000)

  actorSystem.terminate()
}
