package mrks.play.test

import mrks.BaseSpec
import play.api.libs.json.Json


class JsonPropertyMatchersSpec extends BaseSpec with JsonPropertyMatchers {
  case class Data(key: String, value: Int)
  implicit val dataFormat = Json.format[Data]

  private val data = Json.obj(
    "object" -> Data("key", 1),
    "array"  -> Json.arr("a", "b"),
    "string" -> "message",
    "number" -> 123
  )

  "property matcher" should {
    "match property and value" in {
      data must have (
        property ("object", Data("key", 1)),
        property ("object" \ "value", 1),
        property ("array" \ 0, "a"),
        property ("string", "message"),
        property ("number", 123)
      )
    }
    "match inverse property and value" in {
      data must not have (
        property ("object", Data("key", 2)),
        property ("object", Data("other", 1)),
        property ("object" \ "value", 2),
        property ("array" \ 1, "a"),
        property ("string", "error"),
        property ("number", 321)
      )
    }
    "match existing property" in {
      data must have (
        property ("object"),
        property ("string"),
        property ("number")
      )
    }
    "match non-existing property" in {
      data must not have (
        property ("data"),
        property ("str"),
        property ("int")
      )
    }
  }
}
