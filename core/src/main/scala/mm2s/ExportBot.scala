package mm2s

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.stream.ActorMaterializer
import mm4s.bots.api.{Bot, BotID, Ready}
import net.codingwell.scalaguice.ScalaModule


class ExportBot extends Actor with Bot with ActorLogging {
  implicit val mat: ActorMaterializer = ActorMaterializer()


  def uninitialized: Receive = {
    case Ready(api, id) => context.become(ready(api, id))
  }


  def ready(api: ActorRef, id: BotID): Receive = {
    log.debug("ExportBot [{}] ready", id.username)

    {
      case _ ⇒
    }
  }

  def receive: Receive = uninitialized
  override def preStart() = log.debug("ExportBot starting")
}


class ExportBotModule extends ScalaModule {
  def configure() = bind[Bot].to[ExportBot]
}


object ExportBotBoot4dev extends App {
  mm4s.bots.Boot.main(Array.empty)
}
