package domain

import play.api.libs.json.{JsPath, Json}
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._

/**
  * @author Romesh Selvan
  */
case class User(username : String, password : String, role : String)

object User {
  implicit val writer = Json.writes[User]
  implicit val reader = (
    (JsPath \ "username").read[String](minLength[String](1)) and
    (JsPath \ "password").read[String](minLength[String](1)) and
    (JsPath \ "role").read[String](minLength[String](1))
  )(User.apply _)
}
