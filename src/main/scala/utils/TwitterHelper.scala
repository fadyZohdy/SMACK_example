package utils

import entities.Tweet
import org.joda.time.DateTime
import twitter4j.Status
import twitter4j.conf.ConfigurationBuilder


/**
 * Created by droidman on 24/05/16.
 */
  object TwitterHelper {
    val settings = new AppSettings()

    import settings._

    //configuration builder for twitter

    val conf = new ConfigurationBuilder()
      .setDebugEnabled(true)
      .setOAuthConsumerKey(OAuthConsumerKey)
      .setOAuthConsumerSecret(OAuthConsumerSecret)
      .setOAuthAccessToken(OAuthAccessToken)
      .setOAuthAccessTokenSecret(OAuthAccessTokenSecret)
  }


  case class StartCollecting(keyword :String) extends Serializable
  case class InitializeStream(s :String)
  case class KillActor(s :String)
  case class StatusWithKeyword(s: Status, k: String)
