package dynamoDB.converters

import com.amazonaws.services.dynamodbv2.document.Item
import domain.User
import dynamoDB.tableFields.UsersFieldNames.{PASSWORD, ROLE, USERNAME}

/**
  * @author Romesh Selvan
  */
object UserDynamoConverter extends DynamoDBConverter[User] {
  def apply(user : User): Item = new Item().withString(USERNAME, user.username).withString(PASSWORD, user.password).withString(ROLE, user.role)

  def apply(item: Item): User = User(item.getString(USERNAME), item.getString(PASSWORD), item.getString(ROLE))
}
