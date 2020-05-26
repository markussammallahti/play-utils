package mrks.play.mvc

import com.typesafe.config.ConfigException
import mrks.BaseSpec
import play.api.Configuration


class BasicAuthSpec extends BaseSpec {
  import BasicAuth._

  "parse" should {
    "fail" when {
      "non basic scheme" in {
        parse("Bearer token") mustBe None
        parse("Invalid data") mustBe None
      }
      "invalid encoding" in {
        parse("Basic plain:text") mustBe None
      }
      "invalid value" in {
        parse("Basic dmFsdWU=") mustBe None
      }
    }
    "return basic auth" in {
      parse("Basic bGFycnk6a2Vuc2VudG1l") mustBe Some(BasicAuth("larry", "kensentme"))
      parse("basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==") mustBe Some(BasicAuth("Aladdin", "open sesame"))
    }
  }

  "stringify" should {
    "return basic authorization header value" in {
      stringify(BasicAuth("larry", "kensentme")) mustBe "Basic bGFycnk6a2Vuc2VudG1l"
      stringify(BasicAuth("Aladdin", "open sesame")) mustBe "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=="
    }
  }

  "configLoader" should {
    "fail" when {
      "invalid configuration" in {
        val configuration = Configuration(
          "no-usr.password" -> "pwd",
          "no-pwd.username" -> "usr",
        )

        a[ConfigException.Missing] mustBe thrownBy {
          configuration.get[BasicAuth]("no-usr")
        }
        a[ConfigException.Missing] mustBe thrownBy {
          configuration.get[BasicAuth]("no-pwd")
        }
      }
    }
    "load basic auth from configuration" in {
      val configuration = Configuration(
        "first.username"  -> "larry",
        "first.password"  -> "kensentme",
        "second.username" -> "Aladdin",
        "second.password" -> "open sesame",
      )
      configuration.get[BasicAuth]("first") mustBe BasicAuth("larry", "kensentme")
      configuration.get[BasicAuth]("second") mustBe BasicAuth("Aladdin", "open sesame")
    }
  }
}
