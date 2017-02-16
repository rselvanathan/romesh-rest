package dynamoDB

import com.amazonaws.services.dynamodbv2.document.Item
import domain.Family
import tableFields.FamiliesFieldNames._

/**
  * @author Romesh Selvan
  */
object FamilyDynamoConverter extends DynamoDBConverter {

  def apply(item : Item) : Family = {
    Family(item.getString(EMAIL.toString),
      item.getString(FIRST_NAME.toString),
      item.getString(LAST_NAME.toString),
      item.getBoolean(ARE_ATTENDING.toString),
      item.getInt(NUMBER_ATTENDING.toString))
  }

  def apply(family: Family) : Item = {
    new Item()
      .withString(EMAIL.toString, family.email)
      .withString(FIRST_NAME.toString, family.firstName)
      .withString(LAST_NAME.toString, family.lastName)
      .withBoolean(ARE_ATTENDING.toString, family.areAttending)
      .withInt(NUMBER_ATTENDING.toString, family.numberAttending)
  }
}
