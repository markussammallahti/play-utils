package mrks.play.mvc

import play.api.mvc.{ActionFilter, RequestHeader, Result, Results}

import scala.concurrent.{ExecutionContext, Future}


class BasicAuthFilter (
    predicate: BasicAuth => Future[Boolean],
    onUnauthorized: RequestHeader => Result = _ => Results.Unauthorized
  )(implicit val executionContext: ExecutionContext) extends ActionFilter[BasicAuthRequest] {

  override protected def filter[A](request: BasicAuthRequest[A]): Future[Option[Result]] = {
    predicate(request.basicAuth).map {
      case true =>
        None

      case _ =>
        Some(onUnauthorized(request))
    }
  }
}
