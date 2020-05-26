package mrks.play.mvc

import mrks.BaseSpec
import play.api.http.{HeaderNames, Status}
import play.api.mvc.Results
import play.api.test.{DefaultAwaitTimeout, FakeRequest, ResultExtractors}

import scala.concurrent.Future


class BasicAuthFilterSpec extends BaseSpec
    with DefaultAwaitTimeout
    with ResultExtractors
    with HeaderNames
    with Status
    with Results {

  private val predicate = (basicAuth: BasicAuth) => Future.successful(basicAuth.username == "larry")
  private val refiner   = new BasicAuthFilter(predicate)

  private def block(request: BasicAuthRequest[_]) = Future.successful(Ok(request.basicAuth.username))

  "invokeBlock" should {
    "return unauthorized" when {
      "predicate fails" in {
        val request = new BasicAuthRequest(BasicAuth("Aladdin", "open sesame"), FakeRequest())
        val result  = refiner.invokeBlock(request, block)
        status(result) mustBe UNAUTHORIZED
      }
    }
    "execute block" when {
      "predicate passes" in {
        val request = new BasicAuthRequest(BasicAuth("larry", "kensentme"), FakeRequest())
        val result  = refiner.invokeBlock(request, block)
        status(result) mustBe OK
        contentAsString(result) mustBe "larry"
      }
    }
  }
}
