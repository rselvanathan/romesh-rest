package controllers

import com.google.inject.name.Named
import com.google.inject.{Inject, Singleton}
import domain.{Login, User}
import play.api.mvc.{Action, Controller}
import play.api.libs.json.{JsError, JsSuccess, Json}
import repositories.Repo

/**
  * @author Romesh Selvan
  */
@Singleton
class UsersController @Inject() (@Named("User") repo : Repo) extends Controller {

  def getUser = Action { implicit request =>
    val rawJson = request.body.asJson
    if(rawJson.get == null) BadRequest("No Json body was found")
    else {
      rawJson.get.validate[Login] match {
        case loginJson : JsSuccess[Login] =>
          val login = loginJson.get
          val user = repo.findOne(login.username).asInstanceOf[User]
          if(user == null || user.password != login.password) NotFound("User/Password was not found or incorrect")
          else Ok(Json.toJson(user))
        case JsError(error) => BadRequest("JSON Object is incorrect")
      }
    }
  }

  def saveUser = Action { implicit request =>
    val user = repo.save(request.body.asJson.get.as[User].asInstanceOf[repo.T]).asInstanceOf[User]
    Created(Json.toJson(user))
  }
}
