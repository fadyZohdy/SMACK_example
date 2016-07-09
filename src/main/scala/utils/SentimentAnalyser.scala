package utils

import akka.actor.Actor
import akka.actor.Actor.Receive

/**
  * Created by droidman on 7/6/16.
  */
class SentimentAnalyser{

}
object SentimentAnalyser{
  def evaluate(tweet_text: String): Int ={
    scala.util.Random.nextInt(100)
  }

}
