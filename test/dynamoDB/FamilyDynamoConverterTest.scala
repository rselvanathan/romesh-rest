package dynamoDB

import com.amazonaws.services.dynamodbv2.document.Item
import domain.Family
import org.scalatestplus.play.PlaySpec

/**
  * @author Romesh Selvan
  */
class FamilyDynamoConverterTest extends PlaySpec {

  val EMAIL = "email"
  val FIRST_NAME = "romesh"
  val LAST_NAME = "selvan"
  val ARE_ATTENDING = false
  val NUMBER_ATTENDING = 0

  "Converter" must {
    "convert from Family object to DynamoDB Item object" in {
      val family = defaultFamily
      val expected = defaultItem
      val result = FamilyDynamoConverter(family)
      result mustBe expected
    }

    "convert from DynamDB object back to Family object" in {
      val item = defaultItem
      val expected = defaultFamily
      val result = FamilyDynamoConverter(item)
      result mustBe expected
    }
  }

  private def defaultFamily = Family(EMAIL, FIRST_NAME, LAST_NAME, ARE_ATTENDING, NUMBER_ATTENDING)

  private def defaultItem =
    new Item().withString("email", EMAIL)
              .withString("firstName", FIRST_NAME)
              .withString("lastName", LAST_NAME)
              .withBoolean("areAttending", ARE_ATTENDING)
              .withInt("numberAttending", NUMBER_ATTENDING)
}
