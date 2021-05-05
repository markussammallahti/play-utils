package mrks.play.test

import org.scalatest.matchers.{HavePropertyMatchResult, HavePropertyMatcher}
import play.api.libs.json.{Format, JsNull, JsPath, JsValue, Json}

import scala.language.implicitConversions

trait JsonPropertyMatchers {
  implicit def intToJsPath(value: Int): JsPath       = JsPath \ value
  implicit def stringToJsPath(value: String): JsPath = JsPath \ value

  protected def property[A : Format](path: JsPath, expected: A): HavePropertyMatcher[JsValue, JsValue] = HavePropertyMatcher { json =>
    val value = path.asSingleJson(json).getOrElse(JsNull)

    HavePropertyMatchResult(
      value.validate[A].fold(_ => false, _ == expected),
      toPropertyName(path),
      Json.toJson(expected),
      value
    )
  }

  protected def property(path: JsPath): HavePropertyMatcher[JsValue, Boolean] = HavePropertyMatcher { json =>
    val value = path.asSingleJson(json).isDefined

    HavePropertyMatchResult(
      value,
      toPropertyName(path),
      true,
      value
    )
  }

  private def toPropertyName(path: JsPath) = path.path.foldLeft("")((acc, p) => acc + p.toJsonString)
}
