package repositories

import java.util.function.Consumer

import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec
import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item}
import dynamoDB.converters.DynamoDBConverter

import scala.collection.mutable.ListBuffer

/**
  * @author Romesh Selvan
  */
trait Repo[T] {
  def findOne(idColumn : String, id : String)(implicit tableName : String) : Option[T]

  def findAll(implicit tableName : String) : Seq[T]

  def save(_object : T)(implicit tableName : String) : T
}

abstract class RepoImpl[T] (dynamoDB : DynamoDB, dynamoDBConverter: DynamoDBConverter[T]) extends Repo[T] {

  override def findOne(idColumn: String, id: String)(implicit tableName: String): Option[T] = {
    val item = dynamoDB.getTable(tableName).getItem(idColumn, id)
    if(item == null) None
    else Some(dynamoDBConverter(item))
  }

  override def findAll(implicit tableName: String): Seq[T] = {
    val mutableList : ListBuffer[T] = ListBuffer()
    dynamoDB.getTable(tableName).scan(new ScanSpec()).forEach(collectObjects(mutableList))
    mutableList.toList
  }

  override def save(_object: T)(implicit tableName: String): T = {
    val item : Item = dynamoDBConverter.apply(_object)
    dynamoDB.getTable(tableName).putItem(item)
    _object
  }

  private def collectObjects(mutableList : ListBuffer[T]) : Consumer[_ >: Item] = new Consumer[Item] {
    override def accept(item: Item) = {
      mutableList.+=(dynamoDBConverter(item))
    }
  }
}
