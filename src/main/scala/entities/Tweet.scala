package entities

import org.joda.time.DateTime
import twitter4j.Status
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class Tweet(
                  id: Long,
                  createdAt: DateTime,
                  text: String,
                  favouriteCount: Int,
                  retweetCount: Int,
                  keyword: String,
                  user_id: Long
                )


object Tweet {
  def apply(status: Status, keyword: String): Tweet = {
    Tweet(
      id = status.getId,
      createdAt = new DateTime(status.getCreatedAt),
      text = status.getText,
      favouriteCount = status.getFavoriteCount,
      retweetCount = status.getRetweetCount,
      keyword = keyword,
      user_id = status.getUser.getId
    )
  }

  implicit val placeWrites = new Writes[Tweet] {
    def writes(tweet: Tweet) = Json.obj(
      "id" -> tweet.id,
      "createdAt" -> tweet.createdAt,
      "text" -> tweet.text,
      "favouriteCount" -> tweet.favouriteCount,
      "retweetCount" -> tweet.retweetCount,
      "keyword" -> tweet.keyword,
      "user_id" -> tweet.user_id)
  }

  implicit val tweetReads: Reads[Tweet] = (
    (JsPath \ "id").read[Long] and
      (JsPath \ "createdAt").read[DateTime] and
      (JsPath \ "text").read[String] and
      (JsPath \ "favouriteCount").read[Int] and
      (JsPath \ "retweetCount").read[Int] and
      (JsPath \ "keyword").read[String] and
      (JsPath \ "user_id").read[Long]
    )(Tweet.apply(_: Long, _: DateTime, _: String, _: Int, _: Int, _: String, _: Long))
}

