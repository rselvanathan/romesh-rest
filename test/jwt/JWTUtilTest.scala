package jwt

import domain.User
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}
import security.JWTUtil

/**
  * @author Romesh Selvan
  */
class JWTUtilTest extends FunSuite with Matchers with BeforeAndAfterAll {

  val USERNAME = "username"
  val PASSWORD = "password"
  val ROLE = "ADMIN"

  val jwtUtil = new JWTUtil

  override def beforeAll() = {
    sys.props.put("JWTSECRET", "secret")
    sys.props.put("AWS_ACCESS_KEY_ID", "access")
    sys.props.put("AWS_SECRET_ACCESS_KEY", "aws")
  }

  test("Token generated should be a non empty token") {
    val token = jwtUtil.generateToken(defaultUser)
    token should not be null
  }

  test("When retrieving a role from the token, the correct role should be returned") {
    val token = jwtUtil.generateToken(defaultUser)
    val role = jwtUtil.getTokenRole(token)
    role.get should be (ROLE)
  }

  test("When retrieving a username from the token, the correct username should be returned") {
    val token = jwtUtil.generateToken(defaultUser)
    val username = jwtUtil.getTokenUser(token)
    username.get should be (USERNAME)
  }

  test("When retrieving a username and token is bad then expect a None") {
    val username = jwtUtil.getTokenUser("bad")
    username should be (None)
  }

  test("When retrieving a role and token is bad then expect a None") {
    val username = jwtUtil.getTokenRole("bad")
    username should be (None)
  }

  private def defaultUser = User(USERNAME, PASSWORD, ROLE)
}
