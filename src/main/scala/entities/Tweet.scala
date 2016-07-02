package entities

import org.joda.time.DateTime
import twitter4j.Status
import play.api.libs.json._

case class Tweet(
                  id: Long,
                  createdAt: DateTime,
                  text: String,
                  favouriteCount: Int,
                  retweetCount: Int,
                  keyword: String
                )


object Tweet {
  def apply(status: Status, keyword: String): Tweet = {
    Tweet(
      id = status.getId,
      createdAt = new DateTime(status.getCreatedAt),
      text = status.getText,
      favouriteCount = status.getFavoriteCount,
      retweetCount = status.getRetweetCount,
      keyword = keyword
    )
  }

  implicit val placeWrites = new Writes[Tweet] {
    def writes(tweet: Tweet) = Json.obj(
      "id" -> tweet.id,
      "createdAt" -> tweet.createdAt,
      "text" -> tweet.text,
      "favouriteCount" -> tweet.favouriteCount,
      "retweetCount" -> tweet.retweetCount,
      "keyword" -> tweet.keyword)
  }
}

