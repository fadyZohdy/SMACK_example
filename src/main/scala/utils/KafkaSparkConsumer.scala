package utils

import akka.actor.{Actor}
import akka.actor.Actor.Receive
import entities.{TweetByUser, User, Tweet}
import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka.KafkaUtils
import play.api.libs.json.Json

/**
  * Created by droidman on 7/5/16.
  */
class KafkaSparkConsumer(ssc: StreamingContext) extends Actor {

  import AppSettings._

  val topics  = kafkaTopics.map(t => (TopicAndPartition(t, 0), 1L)).toMap

  val kafkaStream =
    KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, List[String]](
      ssc,
      Map("metadata.broker.list" -> bootstrap_servers),
      topics,
      (mm: MessageAndMetadata[String, String]) => List(mm.topic, mm.message))

  kafkaStream.foreachRDD{rdd =>
    if(!rdd.isEmpty()) {
        rdd.foreach{ m =>
          m(0) match {
            case "tweets" =>
              val tweet = Json.parse(m(1)).as[Tweet]
              DBAccess.tweets.store(tweet)
              SentimentAnalyser.evaluate(tweet.text)
            case "users" =>
              val user = Json.parse(m(1)).as[User]
              DBAccess.users.store(user)
            case "tweetByUser" =>
              val tbu = Json.parse(m(1)).as[TweetByUser]
              DBAccess.tweetsByUser.store(tbu)
          }

        }
    }
  }


  ssc.start()
  ssc.awaitTermination()

  override def receive: Receive = ???
}
