package repositories

import com.amazonaws.services.dynamodbv2.document._
import com.amazonaws.services.dynamodbv2.model.ScanResult
import com.amazonaws.services.dynamodbv2.xspec.ScanExpressionSpec
import domain.Family
import dynamoDB.converters.FamilyDynamoConverter
import dynamoDB.tableFields.FamiliesFieldNames
import org.mockito.Mockito._
import org.mockito.Matchers._

import collection.JavaConverters._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FunSuite, Matchers}

/**
  * @author Romesh Selvan
  */
class FamiliesRepoTest extends FunSuite with MockitoSugar with Matchers {

  val EMAIL = "romesh@hotmail.com"

  val FIRSTNAME = "romesh"

  val LASTNAME = "selvan"

  val ATTENDING = true

  val NUMBERATTENDING = 2

  val dynamoDB: DynamoDB = mock[DynamoDB]
  val table: Table = mock[Table]
  val repo : Repo = new FamiliesRepo(dynamoDB, FamilyDynamoConverter)

  test("Families Repo must return a correct family object when requesting for an existing item") {
    when(dynamoDB.getTable("romcharm-families")).thenReturn(table)
      when(table.getItem(FamiliesFieldNames.EMAIL, EMAIL)).thenReturn(defaultItem)
      val result = repo.findOne(EMAIL)
      result should be (defaultFamily)
  }

  test("Families Repo must return all family objects when requesting for all families") {
    new ScanOutcome(new ScanResult().withItems())
    val list = List(defaultItem, defaultItem)
    when(table.scan(any(classOf[ScanExpressionSpec]))).thenReturn(list.asJava)
    val result = repo.findOne(EMAIL)
    result should be (defaultFamily)
  }

  private def defaultFamily = Family(EMAIL, FIRSTNAME, LASTNAME, ATTENDING, NUMBERATTENDING)
  private def defaultItem = new Item().withString(FamiliesFieldNames.EMAIL, EMAIL)
                                        .withString(FamiliesFieldNames.FIRST_NAME, FIRSTNAME)
                                        .withString(FamiliesFieldNames.LAST_NAME, LASTNAME)
                                        .withBoolean(FamiliesFieldNames.ARE_ATTENDING, ATTENDING)
                                        .withInt(FamiliesFieldNames.NUMBER_ATTENDING, NUMBERATTENDING)
}
