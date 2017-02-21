package domain

import org.scalatest.{FunSuite, Matchers}
import play.api.libs.json.Json

/**
  * @author Romesh Selvan
  */
class LoginTest extends FunSuite with Matchers {

  val USERNAME = "romesh"
  val PASSWORD = "password"

  test("When passing in a wrong json Body expect BadRequest") {
    assertLoginJson("{}")
  }

  test("When passing in a login with null username expect BadRequest") {
    assertLoginJson(s"""{"password":"$PASSWORD"}""")
  }

  test("When passing in a login with empty username expect BadRequest") {
    assertLoginJson(s"""{"username":"","password":"$PASSWORD"}""")
  }

  test("When passing in a login with null password expect BadRequest") {
    assertLoginJson(s"""{"username":"$USERNAME"}""")
  }

  test("When passing in a login with empty password expect BadRequest") {
    assertLoginJson(s"""{"username":"$USERNAME","password":""}""")
  }

  private def assertLoginJson(jsonString : String) = {
    val json = Json.parse(jsonString)
    val result = json.validate[Login]
    result.isError should be (true)
  }
}
