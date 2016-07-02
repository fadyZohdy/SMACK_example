package entities

import org.joda.time.DateTime
import play.api.libs.json.{Writes, Json}
import twitter4j.Status

case class TweetByUser(
                        screenName: String,
                        tweet_id: Long,
                        user_id: Long,
                        text: String,
                        createdAt: DateTime
                      )

object TweetByUser {
  def apply(status: Status): TweetByUser = {
    TweetByUser(
      screenName = status.getUser.getScreenName,
      tweet_id = status.getId,
      user_id = status.getUser.getId,
      text = status.getText,
      createdAt = new DateTime(status.getCreatedAt)
    )
  }

  implicit val placeWrites = new Writes[TweetByUser] {
    def writes(tweet: TweetByUser) = Json.obj(
      "screenName" -> tweet.screenName,
      "tweet_id" -> tweet.tweet_id,
      "user_id" -> tweet.user_id,
      "text" -> tweet.text,
      "createdAt" -> tweet.createdAt)
  }
}