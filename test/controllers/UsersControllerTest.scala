package controllers

import controllers.util.{LoginValidationWrapper, UserValidationWrapper}
import defaults.{Roles, TableNames}
import defaults.Roles.Role
import defaults.TableNames.TableName
import domain.{Login, User}
import dynamoDB.tableFields.UsersFieldNames
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}
import play.api.libs.json.{JsValue, Json}
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.test.Helpers.{contentAsString, _}
import repositories.UsersRepo
import security.JWTUtil

/**
  * @author Romesh Selvan
  */
class UsersControllerTest extends FunSuite with Matchers with MockFactory with BeforeAndAfterAll {

  val USERNAME = "romesh"
  val PASSWORD = "password"
  val ROLE = "ROLE_ADMIN"

  val tableName = TableNames.USER_ROLES
  val repo = stub[UsersRepo]
  val controller = new UsersController(repo, LoginValidationWrapper, UserValidationWrapper)

  override def beforeAll = {
    sys.props.put("JWTSECRET", "secret")
    sys.props.put("AWS_ACCESS_KEY_ID", "access")
    sys.props.put("AWS_SECRET_ACCESS_KEY", "aws")
    sys.props.put("AWS_EMAIL_SNS_TOPIC", "TOPIC")
    sys.props.put("APP_TYPE", "APP")
  }

  test("When passing in login information and the information is correct return a Token") {
    (repo.findOne (_:String, _:String)(_:TableName)).when(UsersFieldNames.USERNAME, USERNAME, tableName).returns(Some(defaultUser))
    val request = FakeRequest().withJsonBody(Json.parse(defaultLoginJson))
    val future = controller.authenticate.apply(request)
    val statusCode = status(future)
    val result = contentAsJson(future)

    statusCode should be (200)
  }

  test("When passing in login information and the information and user is not found return NotFound") {
    (repo.findOne (_:String, _:String)(_:TableName)).when(UsersFieldNames.USERNAME, USERNAME, tableName).returns(None)
    val request = FakeRequest().withJsonBody(Json.parse(defaultLoginJson))
    val future = controller.authenticate.apply(request)
    val statusCode = status(future)
    val result = contentAsString(future)

    statusCode should be (404)
    result should be ("User/Password was not found or incorrect")
  }

  test("When passing in login information and the password is incorrect return NotFound") {
    (repo.findOne (_:String, _:String)(_:TableName)).when(UsersFieldNames.USERNAME, USERNAME, tableName).returns(Some(User(USERNAME, "newPass", ROLE)))
    val request = FakeRequest().withJsonBody(Json.parse(defaultLoginJson))
    val future = controller.authenticate.apply(request)
    val statusCode = status(future)
    val result = contentAsString(future)

    statusCode should be (404)
    result should be ("User/Password was not found or incorrect")
  }

  test("When saving a user and the information is correct return a User object") {
    (repo.findOne (_:String, _:String)(_:TableName)).when(UsersFieldNames.USERNAME, USERNAME, tableName).returns(None)
    (repo.save (_:User)(_:TableName)).when(defaultUser, tableName).returns(defaultUser)
    val future = controller.saveUser.apply(getRequest(Roles.ADMIN, Json.parse(defaultUserJson)))
    val statusCode = status(future)
    val result = contentAsJson(future)

    statusCode should be (201)
    result should be (Json.parse(defaultUserJson))
  }

  test("When saving a user and the body is incorrect return a Bad Request") {
    val future = controller.saveUser.apply(getRequest(Roles.ADMIN, Json.parse("{}")))
    val statusCode = status(future)

    statusCode should be (400)
  }

  test("When saving a user and the user already exists then return a 400 error with User already exists message") {
    (repo.findOne (_:String, _:String)(_:TableName)).when(UsersFieldNames.USERNAME, USERNAME, tableName).returns(Some(defaultUser))
    val request = FakeRequest().withJsonBody(Json.parse(defaultUserJson))
    val future = controller.saveUser.apply(getRequest(Roles.ADMIN, Json.parse(defaultUserJson)))
    val statusCode = status(future)
    val result = contentAsString(future)

    statusCode should be (400)
    result should be ("User already exists.")
  }

  test("Only allow saving user as ADMIN") {
    val future = controller.saveUser.apply(getRequest(Roles.MYPAGE_APP, Json.parse("{}")))
    val statusCode = status(future)

    val futureTwo = controller.saveUser.apply(getRequest(Roles.ROMCHARM_APP, Json.parse("{}")))
    val statusCodeTwo = status(futureTwo)

    statusCodeTwo should be (403)
    statusCode should be (403)
  }

  def getRequest(role : Role, jsValue : JsValue) = {
    val token = JWTUtil.generateToken(User("romesh", "password", Roles.getRoleString(role)))
    FakeRequest("GET", "/users/add", FakeHeaders(List("Authorization" -> token).asInstanceOf[Seq[(String, String)]]), null).withJsonBody(jsValue)
  }

  private def defaultLogin = Login(USERNAME, PASSWORD)
  private def defaultUser = User(USERNAME, PASSWORD, ROLE)
  private def defaultLoginJson = s"""{"username":"$USERNAME","password":"$PASSWORD"}"""
  private def defaultUserJson = s"""{"username":"$USERNAME","password":"$PASSWORD","role":"$ROLE"}"""
}
