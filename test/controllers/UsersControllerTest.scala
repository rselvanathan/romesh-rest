package controllers

import controllers.util.{LoginValidationWrapper, UserValidationWrapper}
import domain.{Login, User}
import dynamoDB.tableFields.UsersFieldNames
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers.{contentAsString, _}
import repositories.UsersRepo

/**
  * @author Romesh Selvan
  */
class UsersControllerTest extends FunSuite with Matchers with MockFactory{

  val USERNAME = "romesh"
  val PASSWORD = "password"
  val ROLE = "ROLE_ADMIN"

  val tableName = "romcharm-userRoles"
  val repo = stub[UsersRepo]
  val controller = new UsersController(repo, LoginValidationWrapper, UserValidationWrapper)

  test("When passing in login information and the information is correct return a Token") {
    (repo.findOne (_:String, _:String)(_:String)).when(UsersFieldNames.USERNAME, USERNAME, tableName).returns(Some(defaultUser))
    val request = FakeRequest().withJsonBody(Json.parse(defaultLoginJson))
    val future = controller.authenticate.apply(request)
    val statusCode = status(future)
    val result = contentAsJson(future)

    statusCode should be (200)
  }

  test("When passing in login information and the information and user is not found return NotFound") {
    (repo.findOne (_:String, _:String)(_:String)).when(UsersFieldNames.USERNAME, USERNAME, tableName).returns(None)
    val request = FakeRequest().withJsonBody(Json.parse(defaultLoginJson))
    val future = controller.authenticate.apply(request)
    val statusCode = status(future)
    val result = contentAsString(future)

    statusCode should be (404)
    result should be ("User/Password was not found or incorrect")
  }

  test("When passing in login information and the password is incorrect return NotFound") {
    (repo.findOne (_:String, _:String)(_:String)).when(UsersFieldNames.USERNAME, USERNAME, tableName).returns(Some(User(USERNAME, "newPass", ROLE)))
    val request = FakeRequest().withJsonBody(Json.parse(defaultLoginJson))
    val future = controller.authenticate.apply(request)
    val statusCode = status(future)
    val result = contentAsString(future)

    statusCode should be (404)
    result should be ("User/Password was not found or incorrect")
  }

  test("When saving a user and the information is correct return a User object") {
    (repo.findOne (_:String, _:String)(_:String)).when(UsersFieldNames.USERNAME, USERNAME, tableName).returns(None)
    (repo.save (_:User)(_:String)).when(defaultUser, tableName).returns(defaultUser)
    val request = FakeRequest().withJsonBody(Json.parse(defaultUserJson))
    val future = controller.saveUser.apply(request)
    val statusCode = status(future)
    val result = contentAsJson(future)

    statusCode should be (201)
    result should be (Json.parse(defaultUserJson))
  }

  test("When saving a user and the body is incorrect return a Bad Request") {
    val request = FakeRequest().withJsonBody(Json.parse("{}"))
    val future = controller.saveUser.apply(request)
    val statusCode = status(future)

    statusCode should be (400)
  }

  test("When saving a user and the user already exists then return a 400 error with User already exists message") {
    (repo.findOne (_:String, _:String)(_:String)).when(UsersFieldNames.USERNAME, USERNAME, tableName).returns(Some(defaultUser))
    val request = FakeRequest().withJsonBody(Json.parse(defaultUserJson))
    val future = controller.saveUser.apply(request)
    val statusCode = status(future)
    val result = contentAsString(future)

    statusCode should be (400)
    result should be ("User already exists.")
  }

  private def defaultLogin = Login(USERNAME, PASSWORD)
  private def defaultUser = User(USERNAME, PASSWORD, ROLE)
  private def defaultLoginJson = s"""{"username":"$USERNAME","password":"$PASSWORD"}"""
  private def defaultUserJson = s"""{"username":"$USERNAME","password":"$PASSWORD","role":"$ROLE"}"""
}
