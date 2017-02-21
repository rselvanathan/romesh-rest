package controllers

import controllers.util.FamilyValidationWrapper
import domain.Family
import dynamoDB.tableFields.FamiliesFieldNames
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSuite, Matchers}
import play.api.libs.json.Json
import play.api.mvc.{Result, Results}
import play.api.test.Helpers._
import play.api.test._
import repositories.FamiliesRepo

import scala.concurrent.Future

/**
  * @author Romesh Selvan
  */
class FamiliesControllerTest extends FunSuite with Matchers with MockFactory with Results {

  val EMAIL = "email"
  val FIRST_NAME = "romesh"
  val LAST_NAME = "selvan"
  val ATTENDING = true
  val NUMBERATTENDING = 2

  val tableName = "romcharm-families"
  val familiesRepo = stub[FamiliesRepo]
  val controller = new FamiliesController(familiesRepo, FamilyValidationWrapper)

  test("Family Controller must return a 'Not Found' response when email is not found") {
      (familiesRepo.findOne (_:String, _:String)(_:String)).when(FamiliesFieldNames.EMAIL, EMAIL, tableName).returns(None)
      val result: Future[Result] = controller.findFamily(EMAIL).apply(FakeRequest())
      val statusCode = status(result)
      val string = contentAsString(result)

      statusCode should be (404)
      string should be ("Email was not found")
  }

  test("Family Controller must return a Success response when email is found with correct json") {
    (familiesRepo.findOne (_:String, _:String)(_:String)).when(FamiliesFieldNames.EMAIL, EMAIL, tableName).returns(Some(defaultFamily))
    val result: Future[Result] = controller.findFamily(EMAIL).apply(FakeRequest())
    val statusCode = status(result)
    val string = contentAsString(result)

    statusCode should be (200)
    string should be (defaultFamilyJson)
  }

  test("Family Controller must return Bad request when family already exists when saving") {
    (familiesRepo.findOne (_:String, _:String)(_:String)).when(FamiliesFieldNames.EMAIL, EMAIL, tableName).returns(Some(defaultFamily))
    val request = FakeRequest("POST","/families/add").withJsonBody(Json.parse(defaultFamilyJson))
    val result: Future[Result] = controller.save().apply(request)
    val statusCode = status(result)
    val string = contentAsString(result)

    statusCode should be (400)
    string should be ("Family already exists")
  }

  test("Family Controller must save a valid family json") {
    (familiesRepo.save (_:Family)(_:String)).when(defaultFamily, tableName).returns(defaultFamily)
    (familiesRepo.findOne (_:String, _:String)(_:String)).when(FamiliesFieldNames.EMAIL, EMAIL, tableName).returns(None)
    val request = FakeRequest("POST","/families/add").withJsonBody(Json.toJson(defaultFamily))
    val result: Future[Result] = controller.save().apply(request)
    val statusCode = status(result)
    val string = contentAsString(result)

    statusCode should be (201)
    string should be (defaultFamilyJson)
  }

  def defaultFamily = Family(EMAIL, FIRST_NAME, LAST_NAME, ATTENDING, NUMBERATTENDING)
  def defaultFamilyJson = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""

  def assertBadRequestJson(json : String) = {
    val request = FakeRequest("POST","/families/add").withJsonBody(Json.parse(json))
    val result: Future[Result] = controller.save().apply(request)
    val statusCode = status(result)
    val string = contentAsString(result)

    statusCode should be (400)
    string should be ("JSON Object is incorrect")
  }
}
