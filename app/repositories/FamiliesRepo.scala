package repositories

import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Item}
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec
import com.google.inject.Inject
import com.google.inject.name.Named
import domain.Family
import dynamoDB.converters.DynamoDBConverter
import dynamoDB.tableFields.FamiliesFieldNames

import scala.collection.mutable.ListBuffer

/**
  * @author Romesh Selvan
  */
class FamiliesRepo @Inject() (dynamoDB : DynamoDB, @Named("Family") dynamoDBConverter: DynamoDBConverter) extends Repo {
  override type T = Family

  override def findOne(id: String): T = {
    val item = getTable.getItem(FamiliesFieldNames.EMAIL, id)
    if(item == null) null
    else dynamoDBConverter(item).asInstanceOf[T]
  }

  override def findAll(): Seq[T] = {
    val iterator = getTable.scan(new ScanSpec()).iterator()
    val mutableList : ListBuffer[T] = ListBuffer()
    while (iterator.hasNext) {
      mutableList.+=(dynamoDBConverter(iterator.next()).asInstanceOf[T])
    }
    mutableList.toList
  }

  override def save(_object : T) : T = {
    val item : Item = dynamoDBConverter.apply(_object.asInstanceOf[dynamoDBConverter.T])
    getTable.putItem(item)
    _object
  }

  override def getTable = dynamoDB.getTable("romcharm-families")
}
