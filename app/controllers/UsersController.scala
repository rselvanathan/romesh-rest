package controllers

import com.google.inject.name.Named
import com.google.inject.{Inject, Singleton}
import controllers.util.JsonValidationWrapper
import domain.{Login, User}
import dynamoDB.tableFields.UsersFieldNames
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import repositories.Repo

/**
  * @author Romesh Selvan
  */
@Singleton
class UsersController @Inject() (repo : Repo[User],
                                 @Named("User")jsonWrapper : JsonValidationWrapper) extends Controller {

  implicit val tableName = "romcharm-userRoles"

  def getUser = Action { implicit request =>
    implicit val json = request.body.asJson
    jsonWrapper[Login](login => {
      val user = repo.findOne(UsersFieldNames.USERNAME, login.username)
      if(user.isEmpty || user.get.password != login.password) NotFound("User/Password was not found or incorrect")
      else Ok(Json.toJson(user.get))
    })
  }

  def saveUser = Action { implicit request =>
    val user = repo.save(request.body.asJson.get.as[User])
    Created(Json.toJson(user))
  }
}
