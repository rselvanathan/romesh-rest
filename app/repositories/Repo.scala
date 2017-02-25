package repositories

import java.util.function.Consumer

import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item}
import defaults.TableNames
import defaults.TableNames.TableName
import dynamoDB.converters.DynamoDBConverter

import scala.collection.mutable.ListBuffer

/**
  * @author Romesh Selvan
  */
trait Repo[T] {
  def findOne(idColumn : String, id : String)(implicit tableName : TableName) : Option[T]

  def findAll(implicit tableName : TableName) : Seq[T]

  def save(_object : T)(implicit tableName : TableName) : T
}

abstract class RepoImpl[T] (dynamoDB : DynamoDB, dynamoDBConverter: DynamoDBConverter[T]) extends Repo[T] {

  override def findOne(idColumn: String, id: String)(implicit tableName: TableName): Option[T] = {
    val item = dynamoDB.getTable(TableNames.getTableName(tableName)).getItem(idColumn, id)
    if(item == null) None
    else Some(dynamoDBConverter(item))
  }

  override def findAll(implicit tableName: TableName): Seq[T] = {
    val mutableList : ListBuffer[T] = ListBuffer()
    dynamoDB.getTable(TableNames.getTableName(tableName)).scan(new ScanSpec()).forEach(collectObjects(mutableList))
    mutableList.toList
  }

  override def save(_object: T)(implicit tableName: TableName): T = {
    val item : Item = dynamoDBConverter.apply(_object)
    dynamoDB.getTable(TableNames.getTableName(tableName)).putItem(item)
    _object
  }

  private def collectObjects(mutableList : ListBuffer[T]) : Consumer[_ >: Item] = new Consumer[Item] {
    override def accept(item: Item) = {
      mutableList.+=(dynamoDBConverter(item))
    }
  }
}
