package ivmaykov.com.github

class ArgonautProvider extends JsonProvider {
  import scalaz._, Scalaz._
  import argonaut._, Argonaut._

  override def name: String = "argonaut"

  override def serializeMapToJson(data: Map[String, Seq[Long]]): String = {
    data.asInstanceOf[Map[String, Vector[Long]]].asJson.nospaces
  }

  override def deserializeMapFromJson(json: String): Map[String, Seq[Long]] =
    json.decodeOption[Map[String, Vector[Long]]].get
}
