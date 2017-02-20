package domain

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{JsPath, Json, Reads}

/**
  * @author Romesh Selvan
  */
case class Family(email : String, firstName : String, lastName : String, areAttending : Boolean, numberAttending : Int)

object Family {
  implicit val writer = Json.writes[Family]
  implicit val reader : Reads[Family] = (
      (JsPath \ "email").read[String](minLength[String](1)) and
      (JsPath \ "firstName").read[String](minLength[String](1)) and
      (JsPath \ "lastName").read[String](minLength[String](1)) and
      (JsPath \ "areAttending").read[Boolean] and
      (JsPath \ "numberAttending").read[Int]
    )(Family.apply _)
}