package repositories

import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item}
import com.google.inject.Inject
import domain.User
import dynamoDB.converters.DynamoDBConverter
import dynamoDB.tableFields.UsersFieldNames

import scala.collection.mutable.ListBuffer

/**
  * @author Romesh Selvan
  */
class UsersRepo @Inject() (dynamoDB : DynamoDB, dynamoDBConverter: DynamoDBConverter[User]) extends Repo {
  override type T = User

  override def findOne(id: String): T = {
    val item = getTable.getItem(UsersFieldNames.USERNAME, id)
    if(item == null) null
    else dynamoDBConverter(item)
  }

  override def findAll(): Seq[T] = {
    val iterator = getTable.scan(new ScanSpec()).iterator()
    val mutableList : ListBuffer[T] = ListBuffer()
    while (iterator.hasNext) {
      mutableList.+=(dynamoDBConverter(iterator.next()))
    }
    mutableList.toList
  }

  override def save(_object: T): T = {
    val item : Item = dynamoDBConverter.apply(_object)
    getTable.putItem(item)
    _object
  }

  override def getTable = dynamoDB.getTable("romcharm-userRoles")
}
