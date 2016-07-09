package utils


import akka.actor.Status. {Success, Failure }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import akka.actor._
import org.apache.spark.streaming.StreamingContext

import scala.concurrent.Await

import akka.pattern.gracefulStop
import scala.concurrent.duration._

/**
  * Created by droidman on 27/05/16.
  */
class SupervisorActor(ssc: StreamingContext) extends Actor{

  import AppSettings._

  DB.createTables

  val twitterActor = context.actorOf(Props(new CollectTweetsActor(self)), "twitter-actor")

  implicit val system = context.system
  implicit val materializer = ActorMaterializer()
  implicit val ec =  system.dispatcher

  val producer = new KafkaProducerReactive()

  context.actorOf(Props(new KafkaSparkConsumer(ssc)))

  override def receive = {
    case InitializeStream(s) =>
      twitterActor ! StartCollecting(s)
      println(s, "stream initialized")
  }


  def gracefulShutdown(): Unit = {
    context.children foreach (c => gracefulStop(c, 2.seconds))
  }





  val route =
    path("start_stream"){
      post{
        parameter("query"){q =>
          self ! InitializeStream(q)
          complete("ok")
        }
      }
    }
//    path(""){
//      get{
//        complete{
//
//        }
//      }
//    }

  val serverBinding = Http().bindAndHandle(route, "localhost", 8888)

  }
