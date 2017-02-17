package repositories

import com.amazonaws.services.dynamodbv2.document.{DynamoDB, Table}
import com.google.inject.Inject
import domain.Family
import dynamoDB.converters.DynamoDBConverter
import dynamoDB.tableFields.FamiliesFieldNames

/**
  * @author Romesh Selvan
  */
class FamiliesRepo @Inject() (dynamoDB : DynamoDB, dynamoDBConverter: DynamoDBConverter) extends Repo {
  override type T = Family

  override def findOne(id: String): T = {
    val table = getTable
    dynamoDBConverter(table.getItem(FamiliesFieldNames.EMAIL, id)).asInstanceOf[T]
  }

  override def findAll(): Seq[T] = null

  override def save(_object: T) : T = null

  def getTable = dynamoDB.getTable("romcharm-families")
}
