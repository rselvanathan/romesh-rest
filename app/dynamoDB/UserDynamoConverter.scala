package dynamoDB
import com.amazonaws.services.dynamodbv2.document.Item
import domain.User
import tableFields.UsersFieldNames._

/**
  * @author Romesh Selvan
  */
object UserDynamoConverter extends DynamoDBConverter {
  type T = User

  def apply(user : T): Item = new Item().withString(USERNAME, user.username).withString(PASSWORD, user.password).withString(ROLE, user.role)

  def apply(item: Item): T = User(item.getString(USERNAME), item.getString(PASSWORD), item.getString(ROLE))
}
