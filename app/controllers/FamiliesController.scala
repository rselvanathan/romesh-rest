package controllers

import com.google.inject.{Inject, Singleton}
import domain.Family
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{Action, Controller}
import repositories.FamiliesRepo


/**
  * @author Romesh Selvan
  */
@Singleton
class FamiliesController @Inject() (familiesRepo: FamiliesRepo) extends Controller {

  def findFamily(email : String) = Action {
    val family : Family =  familiesRepo.findOne(email)
    if(family == null) {
      NotFound("Email was not found")
    } else {
      Ok(Json.toJson(family))
    }
  }

  def save() = Action { implicit request =>
    val json = request.body.asJson
    if(json.get == null) BadRequest("No Json body found")
    else {
      json.get.validate[Family] match {
        case success : JsSuccess[Family] => {
          val familySaved : Family = familiesRepo.save(success.get)
          Created(Json.toJson(familySaved))
        }
        case JsError(error) => BadRequest("JSON Object is incorrect")
      }
    }
  }
}
