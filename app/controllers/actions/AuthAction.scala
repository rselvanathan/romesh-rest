package controllers.actions

import akka.util.ByteString
import play.api.http.HttpEntity
import play.api.mvc._
import security.{JWTUtil, PermissionRules, UserRequest}

import scala.concurrent.Future

/**
  * @author Romesh Selvan
  */
object AuthAction extends ActionBuilder[UserRequest] with ActionRefiner[Request, UserRequest]{

  def checkPermission(methodName : String) = new ActionRefiner[UserRequest, UserRequest] {
    override protected def refine[A](request: UserRequest[A]): Future[Either[Result, UserRequest[A]]] = Future.successful {
      if(PermissionRules(methodName, request.role)) Right(request)
      else Left(forbidden("Not Allowed"))
    }
  }

  override protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = Future.successful {
    val optionString = request.headers.get("Authorization")
    if (optionString.isEmpty) Left(forbidden("Authorization header missing"))
    else {
      implicit val token = optionString.get
      val username = JWTUtil.getTokenUser
      val role = JWTUtil.getTokenRole
      if(username.isEmpty || role.isEmpty) Left(forbidden("Incorrect Token"))
      else Right(UserRequest(username.get, role.get, request))
    }
  }

  private def forbidden(text : String) = {
    Result(
      header = ResponseHeader(401, Map.empty),
      body = HttpEntity.Strict(ByteString(text), Some("text/plain"))
    )
  }
}
