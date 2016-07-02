package utilities

import java.util.{Calendar, Date}
import com.websudos.phantom.dsl._
import models.TweetsByUserModel.ConcreteTweetsByUserTable
import models.{UsersModel, TweetsModel}
import TweetsModel.ConcreteTweetsTable
import UsersModel.ConcreteUsersTable
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by droidman on 21/06/16.
  */

object Defaults {
  val connector = ContactPoints(Seq("172.17.0.2")).keySpace("demo")
}


class TwitterDatabase(override val connector: KeySpaceDef) extends Database(connector) {
  // And here you inject the real session and keyspace in the table
  object tweets extends ConcreteTweetsTable with connector.Connector
  object users extends ConcreteUsersTable with connector.Connector
  object tweetsByUser extends ConcreteTweetsByUserTable with connector.Connector
}

object DBAccess extends TwitterDatabase(Defaults.connector)





object DB extends Defaults.connector.Connector{

//  MyDatabase.tweets.store(Tweet(UUID.randomUUID(), "blahblah", 25, 85, "20-8-63"))
//  MyDatabase.users.store(User(123456789, "blah", "blahblah", 123, "location", true))
//  MyDatabase.tweetsByUser.store(TweetByUser.scala("blah", 123456789, 123456789,"text", Calendar.getInstance.getTime))

  def createTables(): Unit = {
    Await.result(DBAccess.autocreate.future(), 5000 millis)
  }
  // val f = MyDatabase.tweets.getBykeyword("GameOfThrones")

  // f.onComplete({
  //   case Success(Some(t)) => println(t.text)
  //   case Success(None) => println("found none")
  //   case Failure(e) => println(e)
  // })
}
