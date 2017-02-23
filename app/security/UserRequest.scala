package security

import play.api.mvc.{Request, WrappedRequest}

/**
  * @author Romesh Selvan
  */
case class UserRequest[A](username : String, role : String, request : Request[A]) extends WrappedRequest[A](request)
