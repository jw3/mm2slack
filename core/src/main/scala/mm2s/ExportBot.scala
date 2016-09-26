package mm2s

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import akka.util.Timeout
import mm2s.ExportBot.ExportChannel
import mm2s.conversions.{ChannelConversion, UserConversion}
import mm4s.api.ChannelModels.ChannelListing
import mm4s.api.Streams._
import mm4s.api._
import mm4s.bots.api._
import net.codingwell.scalaguice.ScalaModule

import scala.concurrent.duration.DurationInt


object ExportBot {

  case class ExportChannel(channelId: String)

}

class ExportBot extends Actor with Bot with ActorLogging {
  implicit val mat: ActorMaterializer = ActorMaterializer()


  def uninitialized: Receive = {
    case Ready(api, id) => context.become(ready(api, id))
  }


  def ready(api: ActorRef, id: BotID): Receive = {
    log.debug("ExportBot [{}] ready", id.username)

    {
      case ExportChannel(chid) ⇒
        import context.system
        import mm4s.api.ChannelProtocols._
        implicit val timeout = Timeout(10 seconds)


        (api ? ConnectionRequest()).mapTo[Connection].flatMap { c ⇒
          Channels.__list.via(c).via(response[ChannelListing]).map { l ⇒
            val users = l.members.values.flatMap(UserConversion(_))
            val channels = l.channels.flatMap(ChannelConversion(_)).map(_.copy(members = users.map(_.id).toList))
          }.runWith(Sink.foreach(println))
        }
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
