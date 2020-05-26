package mrks.play.mvc

import play.api.mvc.{Request, WrappedRequest}


class BasicAuthRequest[+A](val basicAuth: BasicAuth, request: Request[A]) extends WrappedRequest(request) {
  override protected def newWrapper[B](newRequest: Request[B]): WrappedRequest[B] = {
    new BasicAuthRequest[B](basicAuth, newRequest)
  }
}
