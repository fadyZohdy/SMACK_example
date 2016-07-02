package entities

import play.api.libs.json.{Writes, Json}
import twitter4j.Status

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
  implicit val placeWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "name" -> user.name,
      "screenName" -> user.screenName,
      "followersCount" -> user.followersCount,
      "location" -> user.location,
      "isVerified" -> user.isVerified)
  }
}