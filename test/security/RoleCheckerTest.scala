package security

import org.scalatest.{FunSuite, Matchers}

/**
  * @author Romesh Selvan
  */
class RoleCheckerTest extends FunSuite with Matchers {

  val roleChecker = new RoleChecker

  test("When a role checker does not find the particular role against the given endpoint then return false") {
    val path = "/"
    val role = "random"
  }
}
