package utils

import akka.actor.{ActorRef, Actor}
import twitter4j._

/**
 * Created by droidman on 22/05/16.
 */
class CollectTweetsActor(supervisorActor: ActorRef) extends Actor{


  val twitterStreamFactory  = new TwitterStreamFactory(TwitterHelper.cb.build)
  val twitterStream = twitterStreamFactory.getInstance()
  twitterStream.addListener(simpleStatusListener)
  var topic :String = null
  startStream("gameofthrones")

  override def receive = {
    case StartCollecting(keyword) =>
      startStream(keyword)
      topic = keyword
  }


  def startStream(keyWord: String) = {
    twitterStream.cleanUp()
    twitterStream.filter(new FilterQuery().track(keyWord))
    println("***************************"+ keyWord +"*********************************")
  }


  def simpleStatusListener = new StatusListener() {
    def onStatus(status: Status) {
//      println(status.getText)
      context.system.eventStream.publish(StatusWithKeyword(status, "gameofthrones"))
    }
    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
    def onException(ex: Exception) { ex.printStackTrace }
    def onScrubGeo(arg0: Long, arg1: Long) {}
    def onStallWarning(warning: StallWarning) {}
  }



}
