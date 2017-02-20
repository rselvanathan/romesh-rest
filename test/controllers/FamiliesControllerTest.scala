package controllers

import domain.Family
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

  val familiesRepo = stub[FamiliesRepo]
  val controller = new FamiliesController(familiesRepo)

  test("Family Controller must return a 'Not Found' response when email is not found") {
      (familiesRepo.findOne _).when(EMAIL).returns(null)
      val result: Future[Result] = controller.findFamily(EMAIL).apply(FakeRequest())
      val statusCode = status(result)
      val string = contentAsString(result)

      statusCode should be (404)
      string should be ("Email was not found")
  }

  test("Family Controller must return a Success response when email is found with correct json") {
    (familiesRepo.findOne _).when(EMAIL).returns(defaultFamily)
    val result: Future[Result] = controller.findFamily(EMAIL).apply(FakeRequest())
    val statusCode = status(result)
    val string = contentAsString(result)

    statusCode should be (200)
    string should be (defaultFamilyJson)
  }

  test("Family Controller must return Bad request when family already exists when saving") {
    (familiesRepo.findOne _).when(EMAIL).returns(defaultFamily)
    val request = FakeRequest("POST","/families/add").withJsonBody(Json.parse(defaultFamilyJson))
    val result: Future[Result] = controller.save().apply(request)
    val statusCode = status(result)
    val string = contentAsString(result)

    statusCode should be (400)
    string should be ("Family already exists")
  }

  test("Family Controller must save a valid family json") {
    (familiesRepo.save _).when(defaultFamily).returns(defaultFamily)
    val request = FakeRequest("POST","/families/add").withJsonBody(Json.toJson(defaultFamily))
    val result: Future[Result] = controller.save().apply(request)
    val statusCode = status(result)
    val string = contentAsString(result)

    statusCode should be (201)
    string should be (defaultFamilyJson)
  }

  test("Family Controller must return Bad request when no json body is provided when saving") {
    val request = FakeRequest("POST","/families/add").withJsonBody(null)
    val result: Future[Result] = controller.save().apply(request)
    val statusCode = status(result)
    val string = contentAsString(result)

    statusCode should be (400)
    string should be ("No Json body found")
  }

  test("Family Controller must return Bad request when email is null is provided when saving") {
    val json = s"""{"firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("Family Controller must return Bad request when email is empty is provided when saving") {
    val json = s"""{"email":"","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("Family Controller must return Bad request when firstName is null is provided when saving") {
    val json = s"""{"email":"$EMAIL","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("Family Controller must return Bad request when firstName is empty is provided when saving") {
    val json = s"""{"email":"$EMAIL","firstName":"","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("Family Controller must return Bad request when lastName is null is provided when saving") {
    val json = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("Family Controller must return Bad request when lastName is empty is provided when saving") {
    val json = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("Family Controller must return Bad request when areAttending is null is provided when saving") {
    val json = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("Family Controller must return Bad request when attending is wrong format is provided when saving") {
    val json = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":"true","numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("Family Controller must return Bad request when numberAttending is null is provided when saving") {
    val json = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":$ATTENDING}"""
    assertBadRequestJson(json)
  }

  test("Family Controller must return Bad request when numberAttending is wrong format is provided when saving") {
    val json = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":"$NUMBERATTENDING"}"""
    assertBadRequestJson(json)
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
