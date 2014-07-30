package ivmaykov.com.github

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

class JacksonProvider extends JsonProvider {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  override def name: String = "jackson-module-scala"

  override def serializeMapToJson(data: Map[String, Seq[Long]]): String =
    mapper.writeValueAsString(data)

  override def deserializeMapFromJson(json: String): Map[String, Seq[Long]] =
    mapper.readValue[Map[String, Seq[Long]]](json)
}
