package ivmaykov.com.github

class SprayJsonProvider extends JsonProvider {
  import spray.json._
  import spray.json.DefaultJsonProtocol._

  override def name: String = "spray-json"

  override def serializeMapToJson(data: Map[String, Seq[Long]]): String = data.toJson.compactPrint

  override def deserializeMapFromJson(json: String): Map[String, Seq[Long]] =
    json.parseJson.convertTo[Map[String, Seq[Long]]]
}
