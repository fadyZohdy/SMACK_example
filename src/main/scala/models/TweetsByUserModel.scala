package models


import com.websudos.phantom.dsl
import com.websudos.phantom.dsl._
import com.datastax.driver.core.{ConsistencyLevel}
import entities.TweetByUser

import scala.concurrent.Future

/**
  * Created by droidman on 27/06/16.
  */
object TweetsByUserModel {

  class TweetsByUser extends CassandraTable[ConcreteTweetsByUserTable, TweetByUser]{

    object screenName extends StringColumn(this) with PartitionKey[String]
    object tweet_id extends LongColumn(this)
    object user_id extends LongColumn(this)
    object text extends StringColumn(this)
    object createdAt extends DateTimeColumn(this) with ClusteringOrder[DateTime] with Descending

    override def fromRow(row: Row): TweetByUser = {
      TweetByUser(
        screenName(row),
        tweet_id(row),
        user_id(row),
        text(row),
        createdAt(row)
      )
    }


  }

  abstract class ConcreteTweetsByUserTable extends TweetsByUser with RootConnector{

      override lazy val tableName = "tweets_by_user"

      def store(tbu: TweetByUser): Future[ResultSet] = {
        insert.value(_.screenName, tbu.screenName)
            .value(_.tweet_id, tbu.tweet_id)
            .value(_.user_id, tbu.user_id)
            .value(_.text, tbu.text)
            .value(_.createdAt, tbu.createdAt)
            .consistencyLevel_=(ConsistencyLevel.ONE)
            .future()
      }

      def getTweetsByUsername(screenName: String): Future[List[TweetByUser]] = {
        select.where(_.screenName eqs screenName).fetch
      }
  }
}
