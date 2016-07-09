import akka.actor._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkContext, SparkConf}
import utils.{SupervisorActor, AppSettings}


/**
  * Created by droidman on 27/05/16.
  */
object MainApp extends App{

  import AppSettings._

  val system = ActorSystem(appName)

  val conf = new SparkConf().setAppName("test").setMaster("local[*]")

  val ssc = new StreamingContext(conf, Seconds(2))

  val superVisor = system.actorOf(Props(new SupervisorActor(ssc)), "supervisor-actor")
  println(superVisor)

}
