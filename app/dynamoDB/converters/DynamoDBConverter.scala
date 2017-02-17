package dynamoDB.converters

import com.amazonaws.services.dynamodbv2.document.Item

/**
  * @author Romesh Selvan
  */
trait DynamoDBConverter {
  type T

  def apply(_object : T) : Item
  def apply(item : Item) : T
}
