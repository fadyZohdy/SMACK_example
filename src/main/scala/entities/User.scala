package entities

import org.joda.time.DateTime
import play.api.libs.json.{JsPath, Reads, Writes, Json}
import twitter4j.Status
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class User(
                 id: Long,
                 name: String,
                 screenName: String,
                 followersCount: Int,
                 location: String,
                 isVerified: Boolean
               ) extends Serializable

object User {
  def apply(status: Status) : User = {
    User(
      id = status.getUser.getId,
      name = status.getUser.getName,
      screenName = status.getUser.getScreenName,
      followersCount = status.getUser.getFollowersCount,
      location = status.getUser.getLocation,
      isVerified = status.getUser.isVerified
    )
  }
  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "name" -> user.name,
      "screenName" -> user.screenName,
      "followersCount" -> user.followersCount,
      "location" -> user.location,
      "isVerified" -> user.isVerified)
  }

  implicit val tweetReads: Reads[User] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "name").read[String] and
      (JsPath \ "screenName").read[String] and
      (JsPath \ "followersCount").read[Int] and
      (JsPath \ "location").read[String] and
      (JsPath \ "isVerified").read[Boolean]
    )(User.apply(_: Long, _: String, _: String, _: Int, _: String, _: Boolean))
}