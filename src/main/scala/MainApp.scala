import akka.actor._
import org.apache.spark.{SparkContext, SparkConf}
import utils.{SupervisorActor, AppSettings}


/**
  * Created by droidman on 27/05/16.
  */
object MainApp extends App{

  import AppSettings._

  val system = ActorSystem(appName)

  val superVisor = system.actorOf(Props(new SupervisorActor()), "supervisor-actor")
  println(superVisor)

}
