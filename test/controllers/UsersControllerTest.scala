package controllers

import controllers.util.UserValidationWrapper
import domain.{Login, User}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, _}
import repositories.UsersRepo

import scala.concurrent.Future

/**
  * @author Romesh Selvan
  */
class UsersControllerTest extends FunSuite with Matchers with MockFactory{

  val USERNAME = "romesh"
  val PASSWORD = "password"
  val ROLE = "ROLE_ADMIN"

  val repo = stub[UsersRepo]
  val controller = new UsersController(repo, UserValidationWrapper)

  test("When passing in login information and the information is correct return a User object") {
    (repo.findOne _).when(USERNAME).returns(defaultUser)
    val request = FakeRequest().withJsonBody(Json.parse(defaultLoginJson))
    val future = controller.getUser.apply(request)
    val statusCode = status(future)
    val result = contentAsJson(future)

    statusCode should be (200)
    result should be (Json.parse(defaultUserJson))
  }

  test("When passing in login information and the information and user is not found return NotFound") {
    (repo.findOne _).when(USERNAME).returns(null)
    val request = FakeRequest().withJsonBody(Json.parse(defaultLoginJson))
    val future = controller.getUser.apply(request)
    val statusCode = status(future)
    val result = contentAsString(future)

    statusCode should be (404)
    result should be ("User/Password was not found or incorrect")
  }

  test("When passing in login information and the password is incorrect return NotFound") {
    (repo.findOne _).when(USERNAME).returns(User(USERNAME, "newPass", ROLE))
    val request = FakeRequest().withJsonBody(Json.parse(defaultLoginJson))
    val future = controller.getUser.apply(request)
    val statusCode = status(future)
    val result = contentAsString(future)

    statusCode should be (404)
    result should be ("User/Password was not found or incorrect")
  }

  test("When passing in a no json Body expect BadRequest") {
    val request = FakeRequest().withJsonBody(null)
    val future = controller.getUser.apply(request)
    val statusCode = status(future)
    val result = contentAsString(future)

    statusCode should be (400)
    result should be ("No Json body was found")
  }

  test("When passing in a wrong json Body expect BadRequest") {
    assertBadRequestJson("{}")
  }

  test("When passing in a login with null username expect BadRequest") {
    assertBadRequestJson(s"""{"password":"$PASSWORD"}""")
  }

  test("When passing in a login with empty username expect BadRequest") {
    assertBadRequestJson(s"""{"username":"","password":"$PASSWORD"}""")
  }

  test("When passing in a login with null password expect BadRequest") {
    assertBadRequestJson(s"""{"username":"$USERNAME"}""")
  }

  test("When passing in a login with empty password expect BadRequest") {
    assertBadRequestJson(s"""{"username":"$USERNAME","password":""}""")
  }

  test("When saving a user and the information is correct return a User object") {
    (repo.save _).when(defaultUser).returns(defaultUser)
    val request = FakeRequest().withJsonBody(Json.parse(defaultUserJson))
    val future = controller.saveUser.apply(request)
    val statusCode = status(future)
    val result = contentAsJson(future)

    statusCode should be (201)
    result should be (Json.parse(defaultUserJson))
  }

  private def defaultLogin = Login(USERNAME, PASSWORD)
  private def defaultUser = User(USERNAME, PASSWORD, ROLE)
  private def defaultLoginJson = s"""{"username":"$USERNAME","password":"$PASSWORD"}"""
  private def defaultUserJson = s"""{"username":"$USERNAME","password":"$PASSWORD","role":"$ROLE"}"""

  def assertBadRequestJson(json : String) = {
    val request = FakeRequest("POST","/auth").withJsonBody(Json.parse(json))
    val result: Future[Result] = controller.getUser().apply(request)
    val statusCode = status(result)
    val string = contentAsString(result)

    statusCode should be (400)
    string should be ("JSON Object is incorrect")
  }
}
