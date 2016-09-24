package mm2s

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import mm2s.models._
import mm2s.protocols._
import mm4s.api.Streams._
import mm4s.api._


object services {

  def channels(token: String, conn: Connection)(implicit system: ActorSystem, mat: ActorMaterializer): Source[Message, NotUsed] = {
    Channels.list(token).via(conn).via(response[Message])
  }

  def users(team: String, token: String, conn: Connection)(implicit system: ActorSystem, mat: ActorMaterializer): Source[User, NotUsed] = {
    Users.list(team, token).via(conn).via(response[User])
  }

  def messages(channelId: String, since: Long, token: String, conn: Connection)(implicit system: ActorSystem, mat: ActorMaterializer): Source[Message, NotUsed] = {
    Messages.since(since, channelId, token).via(conn).via(response[Message])
  }
}
