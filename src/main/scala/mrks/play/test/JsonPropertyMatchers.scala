package mrks.play.test

import org.scalatest.matchers.{HavePropertyMatchResult, HavePropertyMatcher}
import play.api.libs.json.{Format, JsNull, JsValue, Json}


trait JsonPropertyMatchers {
  protected def property[A : Format](key: String, expected: A): HavePropertyMatcher[JsValue, JsValue] = HavePropertyMatcher { json =>
    val value = (json \ key).getOrElse(JsNull)

    HavePropertyMatchResult(
      value.validate[A].fold(_ => false, _ == expected),
      key,
      Json.toJson(expected),
      value
    )
  }

  protected def property(key: String): HavePropertyMatcher[JsValue, Boolean] = HavePropertyMatcher { json =>
    val value = (json \ key).isDefined

    HavePropertyMatchResult(
      value,
      key,
      true,
      value
    )
  }
}
