package mm2s

import java.nio.file.{Path, Paths}

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Source}
import akka.util.{ByteString, Timeout}
import mm2s.ExportBot.{ExportChannel, _}
import mm2s.conversions.ChannelConversion
import mm4s.api.ChannelModels.ChannelListing
import mm4s.api.Streams._
import mm4s.api._
import mm4s.bots.api._
import net.codingwell.scalaguice.ScalaModule
import spray.json._

import scala.concurrent.duration.DurationInt


object ExportBot {

  case class ExportChannel(channelId: String)

  def temppath(chid: String): Path = {
    Paths.get(s"/tmp/slack-channel-$chid.json")
  }
}

class ExportBot extends Actor with Bot with ActorLogging {
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val timeout = Timeout(10 seconds)

  def uninitialized: Receive = {
    case Ready(api, id) => context.become(ready(api, id))
  }


  def ready(api: ActorRef, id: BotID): Receive = {
    log.debug("ExportBot [{}] ready", id.username)

    {
      case ExportChannel(chid) ⇒
        import context.system
        import mm4s.api.ChannelProtocols._
        import protocols._

        (api ? ConnectionRequest()).mapTo[Connection].flatMap { c ⇒
          Channels.__list.via(c).via(response[ChannelListing])
          .map(l ⇒ l.channels.find(_.id == chid) → l.members.keys) // <-- todo; need some additional filtering on the members; will require a change to the model up in mm4s
          .map {
            case (Some(ch), m) ⇒
              ChannelConversion(ch).map(_.copy(members = m.toList)).map(_.toJson)
            case _ ⇒ None
          }
          .flatMapMerge(3, ch ⇒ Source.fromIterator(() ⇒ ch.iterator).map(_.prettyPrint).map(ByteString(_)))
          .runWith(FileIO.toPath(temppath(chid)))
        }.map {
          case r if r.wasSuccessful ⇒
            api ! Post(s"Download available at ${temppath(chid)}") // representative availability response for now, eventually this is a hyperlink
          case r ⇒
            api ! Post(s"Failed to produce export, ${r.getError.getMessage}")
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
