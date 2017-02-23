package domain

import play.api.libs.json.Json

/**
  * @author Romesh Selvan
  */
case class Token(token : String)

object Token {
  implicit val writer = Json.writes[Token]
}
