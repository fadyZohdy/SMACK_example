package utils

import akka.actor.{ActorRef, Actor}
import akka.kafka.ProducerSettings
import cakesolutions.kafka.{KafkaProducerRecord, KafkaProducer}
import cakesolutions.kafka.akka.{ProducerRecords, KafkaProducerActor}
import cakesolutions.kafka.akka.KafkaProducerActor.MatcherResult
import org.apache.kafka.common.serialization.StringSerializer
import twitter4j._
import utils.{StatusWithKeyword, StartCollecting, AppSettings}

/**
 * Created by droidman on 22/05/16.
 */
class CollectTweetsActor(twitterStreamFactory: TwitterStreamFactory, supervisorActor: ActorRef, settings: AppSettings) extends Actor{

  import settings._


  val twitterStream = twitterStreamFactory.getInstance()
  twitterStream.addListener(simpleStatusListener)
  var topic :String = null

//  val producerActor = context.actorOf(
//    KafkaProducerActor.props[String, String](KafkaProducer.Conf(new StringSerializer, new StringSerializer)),
//    "kafka-producer"
//  )


  override def receive = {
    case StartCollecting(keyword) =>
      startStream(keyword)
      topic = keyword
//      producerActor ! MatcherResult(ProducerRecords.fromValues[String](topic, Seq("hello"), None).records, None)


  }


  def startStream(keyWord: String) = {
    twitterStream.cleanUp()
    twitterStream.filter(new FilterQuery().track(keyWord).language("en"))
  }


  def simpleStatusListener = new StatusListener() {

    def onStatus(status: Status) {
//        producerActor ! MatcherResult(ProducerRecords.fromValuesWithKey[String, String](topic,"key", Seq(status.toString), None).records, None)
//        producerActor ! MatcherResult(Seq(KafkaProducerRecord[Nothing, Status](topic, status)), None)
      context.system.eventStream.publish(StatusWithKeyword(status, topic))
    }
    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
    def onException(ex: Exception) { ex.printStackTrace }
    def onScrubGeo(arg0: Long, arg1: Long) {}
    def onStallWarning(warning: StallWarning) {}
  }



}
