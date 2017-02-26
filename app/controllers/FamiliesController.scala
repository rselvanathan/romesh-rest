package controllers

import com.google.inject.{Inject, Singleton}
import controllers.actions.AuthAction
import controllers.util.JsonValidationWrapper
import defaults.ApiMethods._
import defaults.TableNames
import domain.Family
import dynamoDB.tableFields.FamiliesFieldNames
import notification.EmailNotificationService
import play.api.libs.json.Json
import play.api.mvc.Controller
import repositories.Repo


/**
  * @author Romesh Selvan
  */
@Singleton
class FamiliesController @Inject() ( repo : Repo[Family],
                                     notifier : EmailNotificationService,
                                     jsonValidationWrapper : JsonValidationWrapper[Family]) extends Controller {

  implicit val tableName = TableNames.ROMCHARM_FAMILY

  def findFamily(email : String) = (AuthAction andThen AuthAction.checkPermission(GET_FAMILY)) {
    val family : Option[Family] =  repo.findOne(FamiliesFieldNames.EMAIL, email)
    if(family.isEmpty) {
      NotFound("Email was not found")
    } else {
      Ok(Json.toJson(family))
    }
  }

  def save() = (AuthAction andThen AuthAction.checkPermission(SAVE_FAMILY)) { implicit request =>
    implicit val json = request.body.asJson
    jsonValidationWrapper.apply(family => {
      val familyFound = repo.findOne(FamiliesFieldNames.EMAIL, family.email)
      if(familyFound.isEmpty) {
        val familySaved : Family = repo.save(family)
        notifier.sendEmailNotification(familySaved)
        Created(Json.toJson(repo.save(familySaved)))
      } else {
        BadRequest("Family already exists")
      }
    })
  }
}
