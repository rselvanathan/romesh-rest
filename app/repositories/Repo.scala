package repositories

import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item}
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec
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
    val iterator = dynamoDB.getTable(tableName).scan(new ScanSpec()).iterator()
    val mutableList : ListBuffer[T] = ListBuffer()
    while (iterator.hasNext) {
      mutableList.+=(dynamoDBConverter(iterator.next()))
    }
    mutableList.toList
  }

  override def save(_object: T)(implicit tableName: String): T = {
    val item : Item = dynamoDBConverter.apply(_object)
    dynamoDB.getTable(tableName).putItem(item)
    _object
  }
}
