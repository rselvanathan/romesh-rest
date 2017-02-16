package dynamoDB

import com.amazonaws.services.dynamodbv2.document.Item
import domain.Project

/**
  * @author Romesh Selvan
  */
object ProjectDynamoConverter extends DynamoDBConverter{

  def apply(project : Project) : Item = {
    null
  }

  def apply(item: Item) : Project = {
    null
  }
}
