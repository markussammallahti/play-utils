package mrks.play.mvc

import mrks.BaseSpec
import play.api.http.{HeaderNames, Status}
import play.api.mvc.Results
import play.api.test.{DefaultAwaitTimeout, FakeRequest, ResultExtractors}

import scala.concurrent.Future


class BasicAuthRefinerSpec extends BaseSpec
    with DefaultAwaitTimeout
    with ResultExtractors
    with HeaderNames
    with Status
    with Results {

  private val refiner = new BasicAuthRefiner("Testing")

  private def block(request: BasicAuthRequest[_]) = Future.successful(Ok(request.basicAuth.username))

  "invokeBlock" should {
    "return unauthorized" when {
      "no authorization header" in {
        val request = FakeRequest()
        val result  = refiner.invokeBlock(request, block)
        status(result) mustBe UNAUTHORIZED
        header("WWW-Authenticate", result) mustBe Some("Basic realm=Testing")
      }
      "invalid authorization header" in {
        val request = FakeRequest().withHeaders(AUTHORIZATION -> "Bearer token")
        val result  = refiner.invokeBlock(request, block)
        status(result) mustBe UNAUTHORIZED
        header("WWW-Authenticate", result) mustBe Some("Basic realm=Testing")
      }
    }
    "execute block" when {
      "valid basic auth header" in {
        val request = FakeRequest().withHeaders(AUTHORIZATION -> "Basic bGFycnk6a2Vuc2VudG1l")
        val result  = refiner.invokeBlock(request, block)
        status(result) mustBe OK
        contentAsString(result) mustBe "larry"
      }
    }
  }
}
