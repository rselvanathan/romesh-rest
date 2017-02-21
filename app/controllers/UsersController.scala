package controllers

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
                                 loginWrapper : JsonValidationWrapper[Login],
                                 userWrapper : JsonValidationWrapper[User]) extends Controller {

  implicit val tableName = "romcharm-userRoles"

  def getUser = Action { implicit request =>
    implicit val json = request.body.asJson
    loginWrapper(login => {
      val user = repo.findOne(UsersFieldNames.USERNAME, login.username)
      if(user.isEmpty || user.get.password != login.password) NotFound("User/Password was not found or incorrect")
      else                                                    Ok(Json.toJson(user.get))
    })
  }

  def saveUser = Action { implicit request =>
    implicit val jsn = request.body.asJson
    userWrapper(user => {
      val userFound = repo.findOne(UsersFieldNames.USERNAME, user.username)
      if(userFound.isEmpty) Created(Json.toJson(repo.save(user)))
      else                  BadRequest("User already exists.")
    })
  }
}
