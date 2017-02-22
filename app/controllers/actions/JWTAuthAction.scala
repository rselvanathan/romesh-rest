package controllers.actions

import akka.util.ByteString
import play.api.http.HttpEntity
import play.api.mvc.{Action, Request, ResponseHeader, Result}
import security.JWTUtil

import scala.concurrent.Future

/**
  * @author Romesh Selvan
  */
case class JWTAuthAction[A](jwtUtil : JWTUtil, action : Action[A]) extends Action[A]{

  def apply(request: Request[A]) : Future[Result] = {
    val optionToken = request.headers.get("Authorization")
    if(optionToken.isEmpty) {
      return Future(forbidden("Token not provided"))
    }
    val role = jwtUtil.getTokenRole(optionToken.get)
    val path = request.path
  }

  private def forbidden(text : String) = Result(
    header = ResponseHeader(403, Map.empty),
    body = HttpEntity.Strict(ByteString(text), Some("text/plain"))
  )

  lazy val parser = action.parser
}
