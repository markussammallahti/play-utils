package mrks

import org.scalatest.{MustMatchers, WordSpec}

import scala.concurrent.ExecutionContext


abstract class BaseSpec extends WordSpec with MustMatchers {
  protected implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global
}
