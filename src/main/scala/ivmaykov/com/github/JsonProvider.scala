package ivmaykov.com.github

trait JsonProvider {
  def name: String
  def serializeMapToJson(data: Map[String, Seq[Long]]): String
  def deserializeMapFromJson(json: String): Map[String, Seq[Long]]
}
