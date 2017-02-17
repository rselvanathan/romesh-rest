package dynamoDB.converters

import com.amazonaws.services.dynamodbv2.document.Item
import domain.User
import org.scalatestplus.play.PlaySpec

/**
  * @author Romesh Selvan
  */
class UserDynamoConverterTest extends PlaySpec {

  val USERNAME = "romesh"
  val PASSWORD = "password"
  val ROLE = "ROLE_ADMIN"

  "Converter" must {
    "convert from User object to DynamoDB Item object" in {
      val user = defaultUser
      val expected = defaultItem
      val result = UserDynamoConverter(user)
      result mustBe expected
    }

    "convert from DynamoDB object back to User object" in {
      val item = defaultItem
      val expected = defaultUser
      val result = UserDynamoConverter(item)
      result mustBe expected
    }
  }

  private def defaultUser = User(USERNAME, PASSWORD, ROLE)
  private def defaultItem = new Item().withString("username", USERNAME).withString("password", PASSWORD).withString("role", ROLE)
}
