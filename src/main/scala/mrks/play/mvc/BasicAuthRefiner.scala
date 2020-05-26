package mrks.play.mvc

import play.api.http.HeaderNames
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}


class BasicAuthRefiner(
    realm: String,
    onUnauthorized: RequestHeader => Result = _ => Results.Unauthorized
  )(implicit val executionContext: ExecutionContext) extends ActionRefiner[Request, BasicAuthRequest] {

  override protected def refine[A](request: Request[A]): Future[Either[Result, BasicAuthRequest[A]]] = Future.successful {
    for {
      authorization <- request.headers.get(HeaderNames.AUTHORIZATION).toRight(errorResponse(request))
      basicAuth     <- BasicAuth.parse(authorization).toRight(errorResponse(request))
    } yield {
      new BasicAuthRequest(basicAuth, request)
    }
  }

  private def errorResponse(request: RequestHeader) = {
    onUnauthorized(request).withHeaders("WWW-Authenticate" -> s"Basic realm=$realm")
  }
}
