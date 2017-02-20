package controllers

import com.google.inject.name.Named
import com.google.inject.{Inject, Singleton}
import controllers.util.JsonValidationWrapper
import domain.{Login, User}
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import repositories.Repo

/**
  * @author Romesh Selvan
  */
@Singleton
class UsersController @Inject() (@Named("User") repo : Repo,
                                 @Named("User")jsonWrapper : JsonValidationWrapper) extends Controller {

  def getUser = Action { implicit request =>
    jsonWrapper[Login](request.body.asJson, login => {
      val user = repo.findOne(login.username).asInstanceOf[User]
      if(user == null || user.password != login.password) NotFound("User/Password was not found or incorrect")
      else Ok(Json.toJson(user))
    })
  }

  def saveUser = Action { implicit request =>
    val user = repo.save(request.body.asJson.get.as[User].asInstanceOf[repo.T]).asInstanceOf[User]
    Created(Json.toJson(user))
  }
}
