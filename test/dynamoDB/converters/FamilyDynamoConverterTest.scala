package dynamoDB.converters

import com.amazonaws.services.dynamodbv2.document.Item
import domain.Family
import org.scalatest.{FunSuite, Matchers}

/**
  * @author Romesh Selvan
  */
class FamilyDynamoConverterTest extends FunSuite with Matchers{

  val EMAIL = "email"
  val FIRST_NAME = "romesh"
  val LAST_NAME = "selvan"
  val ARE_ATTENDING = false
  val NUMBER_ATTENDING = 0

  test ("Converter must convert from Family object to DynamoDB Item object") {
      val family = defaultFamily
      val expected = defaultItem
      val result = FamilyDynamoConverter(family)
      result should be (expected)
    }

   test("Coverter must convert from DynamDB object back to Family object") {
      val item = defaultItem
      val expected = defaultFamily
      val result = FamilyDynamoConverter(item)
      result should be (expected)
    }

  private def defaultFamily = Family(EMAIL, FIRST_NAME, LAST_NAME, ARE_ATTENDING, NUMBER_ATTENDING)

  private def defaultItem =
    new Item().withString("email", EMAIL)
              .withString("firstName", FIRST_NAME)
              .withString("lastName", LAST_NAME)
              .withBoolean("areAttending", ARE_ATTENDING)
              .withInt("numberAttending", NUMBER_ATTENDING)
}
