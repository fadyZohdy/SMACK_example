package models

import com.websudos.phantom.CassandraTable
import com.websudos.phantom.dsl._
import entities.Tweet

import scala.concurrent.Future

/**
  * Created by droidman on 27/06/16.
  */
object TweetsModel {

  class Tweets extends CassandraTable[ConcreteTweetsTable, Tweet]{
    object id extends LongColumn(this) with ClusteringOrder[Long] with Ascending
    object text extends StringColumn(this)
    object favouriteCount extends IntColumn(this)
    object retweetcount extends IntColumn(this)
    object createdAt extends DateTimeColumn(this) with ClusteringOrder[DateTime] with Descending
    object keyword extends StringColumn(this) with PartitionKey[String]
    object user_id extends LongColumn(this)

    def fromRow(row: Row): Tweet = {
      Tweet(
        id(row),
        createdAt(row),
        text(row),
        favouriteCount(row),
        retweetcount(row),
        keyword(row),
        user_id(row)
      )
    }
  }

  abstract class ConcreteTweetsTable extends Tweets with RootConnector {

    override lazy val tableName = "tweets"

    def store(tweet: Tweet): Future[ResultSet] = {
      insert.value(_.id, tweet.id)
        .value(_.text, tweet.text)
        .value(_.favouriteCount, tweet.favouriteCount)
        .value(_.retweetcount, tweet.retweetCount)
        .value(_.createdAt, tweet.createdAt)
        .value(_.keyword, tweet.keyword)
        .value(_.user_id, tweet.user_id)
        .consistencyLevel_=(ConsistencyLevel.ONE)
        .future()
    }

    def getBykeyword(keyword: String): Future[Option[Tweet]] = {
      select.where(_.keyword eqs keyword).allowFiltering().one()
    }
  }
}
