package models

import com.websudos.phantom.CassandraTable
import com.websudos.phantom.connectors.RootConnector
import com.websudos.phantom.dsl._
import com.websudos.phantom.keys.{PartitionKey, PrimaryKey}
import entities.User

import scala.concurrent.Future

/**
  * Created by droidman on 27/06/16.
  */
object UsersModel {

  class Users extends CassandraTable[ConcreteUsersTable, User]{

    object id extends LongColumn(this) with PrimaryKey[Long]
    object name extends StringColumn(this)
    object screenName extends StringColumn(this) with PartitionKey[String]
    object followersCount extends IntColumn(this)
    object location extends StringColumn(this)
    object isVerified extends BooleanColumn(this)

    override def fromRow(row: Row): User = {
      User(
        id(row),
        name(row),
        screenName(row),
        followersCount(row),
        location(row),
        isVerified(row)
      )
    }
  }

  abstract class ConcreteUsersTable extends Users with RootConnector{
    override lazy val tableName = "twitter_users"

    def store(user: User) :Future[ResultSet] = {
      insert.value(_.id, user.id)
        .value(_.name, user.name)
        .value(_.screenName, user.screenName)
        .value(_.followersCount, user.followersCount)
        .value(_.location, user.location)
        .value(_.isVerified, user.isVerified)
        .consistencyLevel_=(ConsistencyLevel.ONE)
        .future()
    }

    def getByUsername(username: String) :Future[Option[User]] = {
      select.where(_.screenName eqs username).allowFiltering().one()
    }
  }
}
