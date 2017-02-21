package jwt

import domain.User
import org.scalatest.{FunSuite, Matchers}

/**
  * @author Romesh Selvan
  */
class JWTUtilTest extends FunSuite with Matchers {

  val USERNAME = "username"
  val PASSWORD = "password"
  val ROLE = "ADMIN"

  val jwtUtil = new JWTUtil

  test("Token generated should be a non empty token") {
    val token = jwtUtil.generateToken(defaultUser)
    token should not be null
  }

  test("When retrieving a role from the token, the correct role should be returned") {
    val token = jwtUtil.generateToken(defaultUser)
    val role = jwtUtil.getTokenRole(token)
    role should be (ROLE)
  }

  private def defaultUser = User(USERNAME, PASSWORD, ROLE)
}
