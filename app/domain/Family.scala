package domain

import play.api.libs.json._
import play.api.libs.json.{JsPath, Json, Reads}
import play.api.libs.functional.syntax._

/**
  * @author Romesh Selvan
  */
case class Family(email : String, firstName : String, lastName : String, areAttending : Boolean, numberAttending : Int)

object Family {
  implicit val writer = Json.writes[Family]
  implicit val reader : Reads[Family] = ((__ \ "email").read[String].)
}