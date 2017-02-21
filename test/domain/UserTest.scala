package domain

import org.scalatest.{FunSuite, Matchers}
import play.api.libs.json.Json

/**
  * @author Romesh Selvan
  */
class UserTest extends FunSuite with Matchers {

  val USERNAME = "romesh"
  val PASSWORD = "password"
  val ROLE = "ROLE_ADMIN"

  test("When passing in a user with valid fields then validation should succeed") {
    val jsonString = s"""{"username":"$USERNAME","password":"$PASSWORD","role":"$ROLE"}"""
    val json = Json.parse(jsonString)
    val result = json.validate[User]
    result.isSuccess should be (true)
  }

  test("When passing in a wrong json Body expect BadRequest") {
    assertFailedUserValidation("{}")
  }

  test("When passing in a user with null username expect BadRequest") {
    assertFailedUserValidation(s"""{"password":"$PASSWORD","role":"$ROLE"}""")
  }

  test("When passing in a user with empty username expect BadRequest") {
    assertFailedUserValidation(s"""{"username":"","password":"$PASSWORD","role":"$ROLE"}""")
  }

  test("When passing in a user with null password expect BadRequest") {
    assertFailedUserValidation(s"""{"username":"$USERNAME","role":"$ROLE"}""")
  }

  test("When passing in a user with empty password expect BadRequest") {
    assertFailedUserValidation(s"""{"username":"$USERNAME","password":"","role":"$ROLE"}""")
  }

  test("When passing in a user with null role expect BadRequest") {
    assertFailedUserValidation(s"""{"username":"$USERNAME","password":"$PASSWORD"}""")
  }

  test("When passing in a user with empty role expect BadRequest") {
    assertFailedUserValidation(s"""{"username":"$USERNAME","password":"$PASSWORD","role":""}""")
  }

  private def assertFailedUserValidation(jsonString : String) = {
    val json = Json.parse(jsonString)
    val result = json.validate[User]
    result.isError should be (true)
  }
}
