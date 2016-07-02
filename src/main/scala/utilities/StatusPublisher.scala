package utilities

import akka.actor.Actor.Receive
import akka.stream.actor.ActorPublisher
import entities.Tweet
import twitter4j.Status

/**
  * Created by droidman on 7/2/16.
  */
class StatusPublisher extends ActorPublisher[StatusWithKeyword] {

  val sub = context.system.eventStream.subscribe(self, classOf[StatusWithKeyword])

  override def receive: Receive = {
    case s: StatusWithKeyword =>
      if(isActive && totalDemand > 0) onNext(s)
    case _ =>
  }

  override def postStop(): Unit = {
    context.system.eventStream.unsubscribe(self)
  }
}
