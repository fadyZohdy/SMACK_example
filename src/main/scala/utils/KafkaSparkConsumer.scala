package utils

import akka.actor.Actor
import akka.actor.Actor.Receive
import consumer.kafka.ReceiverLauncher
import entities.Tweet
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import play.api.libs.json.Json

/**
  * Created by droidman on 7/5/16.
  */
class KafkaSparkConsumer extends Actor {

  val conf = new SparkConf().setAppName("test").setMaster("local[*]")

  val ssc = new StreamingContext(conf, Seconds(2))

  val kafkaProperties: Map[String, String] = Map("zookeeper.hosts" -> "40.114.250.188",
    "zookeeper.port" -> "2181",
    "zookeeper.consumer.path" -> "/spark-kafka",
    "zookeeper.broker.path" -> "/brokers" ,
    "kafka.topic" -> "tweets",
    "zookeeper.consumer.connection" -> "40.114.250.188:2181",
    "kafka.consumer.id" -> "12345",
    //optional properties
    "consumer.forcefromstart" -> "true",
    "consumer.backpressure.enabled" -> "true",
    "consumer.fillfreqms" -> "250")

  val props = new java.util.Properties()
  kafkaProperties foreach { case (key,value) => props.put(key, value) }

  val tmp_stream = ReceiverLauncher.launch(ssc, props, 2,StorageLevel.MEMORY_ONLY).map(m => new String(m.getPayload))

  tmp_stream.foreachRDD{rdd =>
    if(!rdd.isEmpty()) {
        rdd.foreach { tweet =>
            val t = Json.parse(tweet).as[Tweet]
            DBAccess.tweets.store(t)
        }
    }
  }

  ssc.start()
  ssc.awaitTermination()

  override def receive: Receive = ???
}
