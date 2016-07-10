package utils

import java.util.Properties

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by droidman on 27/05/16.
  */


object AppSettings extends Serializable {

  val rootConfig = ConfigFactory.load

  val twitter = rootConfig.getConfig("Twitter")
  val OAuthConsumerKey = twitter.getString("OAuthConsumerKey")
  val OAuthConsumerSecret = twitter.getString("OAuthConsumerSecret")
  val OAuthAccessToken = twitter.getString("OAuthAccessToken")
  val OAuthAccessTokenSecret = twitter.getString("OAuthAccessTokenSecret")

  val insider = rootConfig.getConfig("Insider")
  val appName = insider.getString("app-name")

  val kafka = rootConfig.getConfig("kafka")
  val noOfPartitions = kafka.getInt("number.of.partitions")
  val bootstrap_servers = kafka.getString("bootstrab_servers")
  val key_serializer = kafka.getString("serializer")
  val value_serializer = kafka.getString("serializer")
  val zookeeper = kafka.getString("zookeeper.connect")
  val kafkaTopics = kafka.getString("topics").split(",").toList



  val spark = rootConfig.getConfig("spark")
  val sparkMaster = spark.getString("master")
  val StreamingBatchInterval = spark.getInt("streaming.batch.interval")
  val streamingCheckpoint = spark.getString("spark.checkpoint.dir")

  val cassandra = rootConfig.getConfig("cassandra")
  val hosts = cassandra.getString("hosts").split(",").toSeq
  val keyspace = cassandra.getString("keyspace")

  val alchemyAPI = rootConfig.getConfig("AlchemyAPI")
  val apiKey = alchemyAPI.getString("credentials.apikey")
}
