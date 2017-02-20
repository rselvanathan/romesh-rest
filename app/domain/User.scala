package domain

import play.api.libs.json.Json

/**
  * @author Romesh Selvan
  */
case class User(username : String, password : String, role : String)

object User {
  implicit val writer = Json.writes[User]
  implicit val reader = Json.reads[User]
}
