import akka.actor._
import utils.{SupervisorActor, AppSettings}


/**
  * Created by droidman on 27/05/16.
  */
object MainApp extends App{

  val settings = new AppSettings()
  import settings._

  val system = ActorSystem(appName)

  val superVisor = system.actorOf(Props(new SupervisorActor(settings)), "supervisor-actor")
  println(superVisor)

}
