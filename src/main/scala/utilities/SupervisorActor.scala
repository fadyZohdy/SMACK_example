package utilities


import akka.actor.Status. {Success, Failure }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import akka.actor._
import utilities.TwitterHelper.{conf}

import scala.concurrent.Await

import twitter4j.TwitterStreamFactory
import akka.pattern.gracefulStop
import scala.concurrent.duration._
import akka.pattern.ask


/**
  * Created by droidman on 27/05/16.
  */
class SupervisorActor(settings: AppSettings) extends Actor{

  import settings._


  val tf  = new TwitterStreamFactory(conf.build)
  val twitterActor = context.actorOf(Props(new CollectTweetsActor(tf, self, settings)), "twitter-actor")

  implicit val system = context.system
  implicit val materializer = ActorMaterializer()

  val producer = new ReactiveKafkaProducer()



  override def receive = {
    case InitializeStream(s) =>
      twitterActor ! StartCollecting(s)
      println(s, "stream initialized")
  }


  def gracefulShutdown(): Unit = {
    context.children foreach (c => gracefulStop(c, 2.seconds))
  }




  implicit val ec =  system.dispatcher
  val route =
    pathPrefix("twitter_stream") {
      (get & path(Segment)) { keyword =>
        self ! InitializeStream(keyword)
        complete{
          "ok"
        }
      }
    }

  val serverBinding = Http().bindAndHandle(route, "localhost", 8888)

  }
