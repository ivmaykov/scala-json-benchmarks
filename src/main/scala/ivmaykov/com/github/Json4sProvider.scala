package ivmaykov.com.github

class Json4sNativeProvider extends JsonProvider {
  import org.json4s._
  import org.json4s.native.JsonMethods._

  override def name: String = "json4s (native)"

  override def serializeMapToJson(data: Map[String, Seq[Long]]): String = {
    val jsonObject = JObject(data.map { case (key, values) =>
      key -> JArray(values.map { v => JInt(BigInt(v)) }.toList)
    }.toList)
    compact(render(jsonObject))
  }

  override def deserializeMapFromJson(json: String): Map[String, Seq[Long]] = {
    val parsed = parse(json).asInstanceOf[JObject]
    parsed.values.map { case (key, values) =>
      key -> values.asInstanceOf[List[BigInt]].map { _.toLong }
    }
  }
}

class Json4sJacksonProvider extends JsonProvider {
  import org.json4s._
  import org.json4s.jackson.JsonMethods._

  override def name: String = "json4s (jackson)"

  override def serializeMapToJson(data: Map[String, Seq[Long]]): String = {
    val jsonObject = JObject(data.map { case (key, values) =>
      key -> JArray(values.map { v => JInt(BigInt(v)) }.toList)
    }.toList)
    compact(render(jsonObject))
  }

  override def deserializeMapFromJson(json: String): Map[String, Seq[Long]] = {
    val parsed = parse(json).asInstanceOf[JObject]
    parsed.values.map { case (key, values) =>
      key -> values.asInstanceOf[List[BigInt]].map { _.toLong }
    }
  }
}
