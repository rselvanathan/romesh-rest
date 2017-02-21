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
class FamiliesController @Inject() (@Named("Family")repo: Repo,
                                    @Named("Family")jsonValidationWrapper : JsonValidationWrapper) extends Controller {

  def findFamily(email : String) = Action {
    val family : Family =  repo.findOne(email).asInstanceOf[Family]
    if(family == null) {
      NotFound("Email was not found")
    } else {
      Ok(Json.toJson(family))
    }
  }

  def save() = Action { implicit request =>
    implicit val json = request.body.asJson
    jsonValidationWrapper.apply[Family](family => {
      val familyFound = repo.findOne(family.email)
      if(familyFound == null) {
        val familySaved : Family = repo.save(family.asInstanceOf[repo.T]).asInstanceOf[Family]
        Created(Json.toJson(familySaved))
      } else {
        BadRequest("Family already exists")
      }
    })
  }
}
