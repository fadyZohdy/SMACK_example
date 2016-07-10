package utils


import java.util

import com.ibm.watson.developer_cloud.alchemy.v1.AlchemyLanguage
import play.api.libs.json._

/**
  * Created by droidman on 7/6/16.
  */

object SentimentAnalyser{
  import AppSettings._

  val service = new AlchemyLanguage()
  service.setApiKey(apiKey)

  def evaluate(tweet_text: String): Int ={

    val params = new util.HashMap[String, AnyRef]
    params.put(AlchemyLanguage.TEXT, tweet_text)

    val sentiment = service.getSentiment(params).execute().toString

    val json = Json.parse(sentiment)

    val score = (json \ "docSentiment" \"type").get.toString

    score match {
      case "positive" => 1
      case "negative" => -1
      case "neutral" => 0
    }
  }

}
