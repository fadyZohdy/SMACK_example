package utilities

import akka.actor.Actor.Receive
import akka.actor.{Props, Actor, ActorSystem}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.{ClosedShape, ActorMaterializer}
import akka.stream.scaladsl._
import entities.{TweetByUser, User, Tweet}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import play.api.libs.json.Json
import twitter4j.Status

/**
  * Created by droidman on 7/2/16.
  */
class ReactiveKafkaProducer(implicit val system: ActorSystem, implicit val materializer: ActorMaterializer){


  val producerSettings = ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
    .withBootstrapServers("localhost:9092")

  val source = Source.actorPublisher[StatusWithKeyword](Props[StatusPublisher])

  val tweetExtractor = Flow[StatusWithKeyword]
                        .map(s => Tweet(s.s, s.k))
                        .map(t => new ProducerRecord[Array[Byte], String]("tweets", Json.toJson(t).toString()))

  val userExtractor = Flow[StatusWithKeyword]
                        .map(s => User(s.s))
                        .map(u => new ProducerRecord[Array[Byte], String]("users", Json.toJson(u).toString()))

  val tweetByUserExtractor = Flow[StatusWithKeyword]
                        .map(s => TweetByUser(s.s))
                        .map(tbu => new ProducerRecord[Array[Byte], String]("tweetByUser", Json.toJson(tbu).toString()))

  val sink = Producer.plainSink(producerSettings)

  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val broadcast = b.add(Broadcast[StatusWithKeyword](3))
    source ~> broadcast ~> tweetExtractor ~> sink
              broadcast ~> userExtractor ~> sink
              broadcast ~> tweetByUserExtractor ~> sink
    ClosedShape
  }).run()

}
