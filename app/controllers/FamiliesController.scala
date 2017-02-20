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
    jsonValidationWrapper.apply[Family](request.body.asJson, success => {
      val family = repo.findOne(success.email)
      if(family == null) {
        val familySaved : Family = repo.save(success.asInstanceOf[repo.T]).asInstanceOf[Family]
        Created(Json.toJson(familySaved))
      } else {
        BadRequest("Family already exists")
      }
    })
  }
}
