package mm2s

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}


object models {

  case class User(id: String,
                  team_id: String,
                  name: String,
                  deleted: Boolean,
                  color: String,
                  real_name: String,
                  tz: String,
                  tz_label: String,
                  tz_offset: Int,
                  profile: UserProfile)

  case class UserProfile(first_name: String,
                         last_name: String,
                         real_name: String,
                         real_name_normalized: String,
                         email: String)

  case class Channel(id: String,
                     name: String,
                     created: Long,
                     creator: String,
                     is_archived: Boolean,
                     is_general: Boolean,
                     members: Seq[String],
                     topic: Topic,
                     purpose: Purpose)

  case class Topic(value: String,
                   creator: String,
                   last_set: Int)

  case class Purpose(value: String,
                     creator: String,
                     last_set: Int)


  case class Message(user: String,
                     text: String,
                     `type`: String,
                     subtype: Option[String],
                     ts: Double)
}

object protocols extends DefaultJsonProtocol with SprayJsonSupport {
  import models._

  implicit val UserProfileFormat: RootJsonFormat[UserProfile] = jsonFormat5(UserProfile)
  implicit val UserFormat: RootJsonFormat[User] = jsonFormat10(User)

  implicit val TopicFormat: RootJsonFormat[Topic] = jsonFormat3(Topic)
  implicit val PurposeFormat: RootJsonFormat[Purpose] = jsonFormat3(Purpose)
  implicit val MessageFormat: RootJsonFormat[Message] = jsonFormat5(Message)
  implicit val ChannelFormat: RootJsonFormat[Channel] = jsonFormat9(Channel)
}
