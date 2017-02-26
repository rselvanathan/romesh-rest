package controllers

import controllers.util.FamilyValidationWrapper
import defaults.{Roles, TableNames}
import defaults.Roles.Role
import defaults.TableNames.TableName
import domain.{Family, User}
import dynamoDB.tableFields.FamiliesFieldNames
import notification.EmailNotificationService
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}
import play.api.libs.json.Json
import play.api.mvc.{Result, Results}
import play.api.test.Helpers._
import play.api.test._
import repositories.FamiliesRepo
import security.JWTUtil

import scala.concurrent.Future

/**
  * @author Romesh Selvan
  */
class FamiliesControllerTest extends FunSuite with Matchers with MockFactory with Results with BeforeAndAfterAll {

  val EMAIL = "email"
  val FIRST_NAME = "romesh"
  val LAST_NAME = "selvan"
  val ATTENDING = true
  val NUMBERATTENDING = 2

  val tableName = TableNames.ROMCHARM_FAMILY
  val familiesRepo = stub[FamiliesRepo]
  val notifier = stub[EmailNotificationService]
  val controller = new FamiliesController(familiesRepo,notifier,FamilyValidationWrapper)

  override def beforeAll = {
    sys.props.put("JWTSECRET", "secret")
    sys.props.put("AWS_ACCESS_KEY_ID", "access")
    sys.props.put("AWS_SECRET_ACCESS_KEY", "aws")
    sys.props.put("AWS_EMAIL_SNS_TOPIC", "TOPIC")
    sys.props.put("APP_TYPE", "APP")
  }

  test("Family Controller must return a 'Not Found' response when email is not found") {
      (familiesRepo.findOne (_:String, _:String)(_:TableName)).when(FamiliesFieldNames.EMAIL, EMAIL, tableName).returns(None)
      val result: Future[Result] = controller.findFamily(EMAIL).apply(getRequest(Roles.ROMCHARM_APP))
      val statusCode = status(result)
      val string = contentAsString(result)

      statusCode should be (404)
      string should be ("Email was not found")
  }

  test("Family Controller must return a Success response when email is found with correct json") {
    (familiesRepo.findOne (_:String, _:String)(_:TableName)).when(FamiliesFieldNames.EMAIL, EMAIL, tableName).returns(Some(defaultFamily))
    val result: Future[Result] = controller.findFamily(EMAIL).apply(getRequest(Roles.ROMCHARM_APP))
    val statusCode = status(result)
    val string = contentAsString(result)

    statusCode should be (200)
    string should be (defaultFamilyJson)
  }

  test("If find is called with a role other than ROMCHARM or Admin then expect a Forbidden response") {
    val result: Future[Result] = controller.findFamily(EMAIL).apply(getRequest(Roles.MYPAGE_APP))
    val statusCode = status(result)

    statusCode should be (403)
  }

  test("Family Controller must return Bad request when family already exists when saving") {
    (familiesRepo.findOne (_:String, _:String)(_:TableName)).when(FamiliesFieldNames.EMAIL, EMAIL, tableName).returns(Some(defaultFamily))
    val token = JWTUtil.generateToken(User("romesh", "password", Roles.getRoleString(Roles.ROMCHARM_APP)))
    val request = FakeRequest("POST", "/families/add", FakeHeaders(List("Authorization" -> token)), null).withJsonBody(Json.parse(defaultFamilyJson))
    val result: Future[Result] = controller.save().apply(request)
    val statusCode = status(result)
    val string = contentAsString(result)

    statusCode should be (400)
    string should be ("Family already exists")
  }

  test("Family Controller must save a valid family json") {
    (familiesRepo.save (_:Family)(_:TableName)).when(defaultFamily, tableName).returns(defaultFamily)
    (familiesRepo.findOne (_:String, _:String)(_:TableName)).when(FamiliesFieldNames.EMAIL, EMAIL, tableName).returns(None)
    val token = JWTUtil.generateToken(User("romesh", "password", Roles.getRoleString(Roles.ROMCHARM_APP)))
    val request = FakeRequest("POST", "/families/add", FakeHeaders(List("Authorization" -> token)), null).withJsonBody(Json.parse(defaultFamilyJson))
    val result: Future[Result] = controller.save.apply(request)
    val statusCode = status(result)
    val string = contentAsString(result)

    statusCode should be (201)
    string should be (defaultFamilyJson)
  }

  test("If save is called with a role other than ROMCHARM or Admin then expect a Forbidden response") {
    val token = JWTUtil.generateToken(User("romesh", "password", Roles.getRoleString(Roles.MYPAGE_APP)))
    val request = FakeRequest("POST", "/families/add", FakeHeaders(List("Authorization" -> token)), null).withJsonBody(Json.parse(defaultFamilyJson))
    val result: Future[Result] = controller.save.apply(request)
    val statusCode = status(result)

    statusCode should be (403)
  }

  def getRequest(role : Role) = {
    val token = JWTUtil.generateToken(User("romesh", "password", Roles.getRoleString(role)))
    FakeRequest("GET", "/", FakeHeaders(List("Authorization" -> token).asInstanceOf[Seq[(String, String)]]), null)
  }

  def defaultFamily = Family(EMAIL, FIRST_NAME, LAST_NAME, ATTENDING, NUMBERATTENDING)
  def defaultFamilyJson = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
}
