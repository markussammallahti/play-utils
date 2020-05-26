package mrks.play.mvc

import java.nio.charset.StandardCharsets

import org.apache.commons.codec.binary.{Base64, StringUtils}
import play.api.ConfigLoader

import scala.util.Try


case class BasicAuth(username: String, password: String)

object BasicAuth {
  val SchemeName: String = "Basic"

  def parse(input: String): Option[BasicAuth] = {
    for {
      encoded              <- extract(input)
      decoded              <- decode(encoded)
      (username, password) <- split(decoded)
    } yield {
      BasicAuth(username, password)
    }
  }

  private def extract(input: String) = {
    input.split(' ') match {
      case Array(scheme, encoded) if scheme.equalsIgnoreCase(SchemeName) =>
        Some(encoded)

      case _ =>
        None
    }
  }

  private def decode(input: String) = {
    Try(StringUtils.newStringUtf8(Base64.decodeBase64(input))).toOption
  }

  private def split(input: String) = {
    input.split(':') match {
      case Array(username, password) =>
        Some(username -> password)

      case _ =>
        None
    }
  }

  def stringify(auth: BasicAuth): String = {
    stringify(auth.username, auth.password)
  }

  def stringify(username: String, password: String): String = {
    s"Basic ${Base64.encodeBase64String(s"$username:$password".getBytes(StandardCharsets.UTF_8))}"
  }

  implicit val configLoader: ConfigLoader[BasicAuth] = ConfigLoader { config => path =>
    BasicAuth(
      username = config.getString(s"$path.username"),
      password = config.getString(s"$path.password")
    )
  }
}
