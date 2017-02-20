package domain

import play.api.libs.json.{JsPath, Reads}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

/**
  * @author Romesh Selvan
  */
case class Login(username : String, password : String)

object Login {
  implicit val reader : Reads[Login] = (
    (JsPath \ "username").read[String](minLength[String](1)) and
    (JsPath \ "password").read[String](minLength[String](1))
  )(Login.apply _)
}
