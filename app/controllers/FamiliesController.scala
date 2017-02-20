package controllers

import com.google.inject.name.Named
import com.google.inject.{Inject, Singleton}
import domain.Family
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{Action, Controller}
import repositories.Repo


/**
  * @author Romesh Selvan
  */
@Singleton
class FamiliesController @Inject() (@Named("Family")repo: Repo) extends Controller {

  def findFamily(email : String) = Action {
    val family : Family =  repo.findOne(email).asInstanceOf[Family]
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
        case success : JsSuccess[Family] =>
          val familiy = repo.findOne(success.get.email)
          if(familiy == null) {
            val familySaved : Family = repo.save(success.get.asInstanceOf[repo.T]).asInstanceOf[Family]
            Created(Json.toJson(familySaved))
          } else {
            BadRequest("Family already exists")
          }
        case JsError(error) => BadRequest("JSON Object is incorrect")
      }
    }
  }
}
