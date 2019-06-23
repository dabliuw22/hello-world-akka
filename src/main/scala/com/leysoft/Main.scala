package com.leysoft

import akka.actor.ActorSystem
import com.leysoft.actor._

object Main extends App {
  val actorSystem = ActorSystem("my-actor-system")

  val storeUserActor = actorSystem.actorOf(StoreUserActor.props, "store-actor")
  val checkerUserActor = actorSystem.actorOf(CheckerUserActor.props(storeUserActor), "checker-actor")
  val userActor = actorSystem.actorOf(UserActor.props(checkerUserActor), "user-actor")
  userActor ! User("username1", "username1@mail.com")

  val supervisorActor = actorSystem.actorOf(SupervisorActor.props, "supervisor-actor")
  supervisorActor ! "STOP"

  val randomRouterActor = actorSystem.actorOf(RandomRouterActor.props)
  randomRouterActor ! Work("Name1", "Description1")

  val parallelActor = actorSystem.actorOf(ParallelActor.props, "parallel-actor")
  parallelActor ! (1, 2)

  Thread.sleep(5000)

  actorSystem.terminate()
}
