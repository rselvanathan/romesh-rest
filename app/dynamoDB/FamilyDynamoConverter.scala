package dynamoDB

import com.amazonaws.services.dynamodbv2.document.Item
import domain.Family
import tableFields.FamiliesFieldNames._

/**
  * @author Romesh Selvan
  */
object FamilyDynamoConverter extends DynamoDBConverter {

  def apply(item : Item) : Family = {
    Family(item.getString(EMAIL),
      item.getString(FIRST_NAME),
      item.getString(LAST_NAME),
      item.getBoolean(ARE_ATTENDING),
      item.getInt(NUMBER_ATTENDING))
  }

  def apply(family: Family) : Item = {
    new Item()
      .withString(EMAIL, family.email)
      .withString(FIRST_NAME, family.firstName)
      .withString(LAST_NAME, family.lastName)
      .withBoolean(ARE_ATTENDING, family.areAttending)
      .withInt(NUMBER_ATTENDING, family.numberAttending)
  }
}
