package controllers

import com.google.inject.name.Named
import com.google.inject.{Inject, Singleton}
import controllers.util.JsonValidationWrapper
import domain.Family
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import repositories.Repo


/**
  * @author Romesh Selvan
  */
@Singleton
class FamiliesController @Inject() ( repo : Repo[Family],
                                     @Named("Family")jsonValidationWrapper : JsonValidationWrapper) extends Controller {

  implicit val tableName = "romcharm-families"

  def findFamily(email : String) = Action {
    val family : Option[Family] =  repo.findOne("email", email)
    if(family.isEmpty) {
      NotFound("Email was not found")
    } else {
      Ok(Json.toJson(family))
    }
  }

  def save() = Action { implicit request =>
    implicit val json = request.body.asJson
    jsonValidationWrapper.apply[Family](family => {
      val familyFound = repo.findOne("email", family.email)
      if(familyFound.isEmpty) {
        val familySaved : Family = repo.save(family)
        Created(Json.toJson(familySaved))
      } else {
        BadRequest("Family already exists")
      }
    })
  }
}
