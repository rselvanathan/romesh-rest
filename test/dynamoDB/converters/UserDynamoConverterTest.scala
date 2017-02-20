package dynamoDB.converters

import com.amazonaws.services.dynamodbv2.document.Item
import domain.User
import org.scalatest.{FunSuite, Matchers}
import org.scalatestplus.play.PlaySpec

/**
  * @author Romesh Selvan
  */
class UserDynamoConverterTest extends FunSuite with Matchers {

  val USERNAME = "romesh"
  val PASSWORD = "password"
  val ROLE = "ROLE_ADMIN"

  test("Converter must convert from User object to DynamoDB Item object") {
      val user = defaultUser
      val expected = defaultItem
      val result = UserDynamoConverter(user)
      result should be (expected)
    }

  test("Converter must convert from DynamoDB object back to User object") {
      val item = defaultItem
      val expected = defaultUser
      val result = UserDynamoConverter(item)
      result should be (expected)
    }

  private def defaultUser = User(USERNAME, PASSWORD, ROLE)
  private def defaultItem = new Item().withString("username", USERNAME).withString("password", PASSWORD).withString("role", ROLE)
}
