package domain

import org.scalatest.{FunSuite, Matchers}
import play.api.libs.json.Json

/**
  * @author Romesh Selvan
  */
class FamilyTest extends FunSuite with Matchers {

  val EMAIL = "email"
  val FIRST_NAME = "romesh"
  val LAST_NAME = "selvan"
  val ATTENDING = true
  val NUMBERATTENDING = 2

  test("When Family JSON passed in is correct then expect a success") {
    val jsonString = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    val json = Json.parse(jsonString)
    val result = json.validate[Family]
    result.isSuccess should be (true)
  }

  test("JSON Validation should fail when email of null is provided") {
    val json = s"""{"firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("JSON Validation should fail when email of empty is provided") {
    val json = s"""{"email":"","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("JSON Validation should fail when firstName of null is provided") {
    val json = s"""{"email":"$EMAIL","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("JSON Validation should fail when firstName of empty is provided") {
    val json = s"""{"email":"$EMAIL","firstName":"","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("JSON Validation should fail when lastName of null is provided") {
    val json = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("JSON Validation should fail when lastName of empty is provided") {
    val json = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"","areAttending":$ATTENDING,"numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("JSON Validation should fail when areAttending of null is provided") {
    val json = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("JSON Validation should fail when attending with wrong format is provided") {
    val json = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":"true","numberAttending":$NUMBERATTENDING}"""
    assertBadRequestJson(json)
  }

  test("JSON Validation should fail when numberAttending of null is provided") {
    val json = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":$ATTENDING}"""
    assertBadRequestJson(json)
  }

  test("JSON Validation should fail when numberAttending with wrong format is provided") {
    val json = s"""{"email":"$EMAIL","firstName":"$FIRST_NAME","lastName":"$LAST_NAME","areAttending":$ATTENDING,"numberAttending":"$NUMBERATTENDING"}"""
    assertBadRequestJson(json)
  }

  private def assertBadRequestJson(jsonString : String) = {
    val json = Json.parse(jsonString)
    val result = json.validate[Family]
    result.isError should be (true)
  }
}
