//package repositories
//
//import com.amazonaws.services.dynamodbv2.document._
//import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec
//import dynamoDB.converters.ProjectDynamoConverter
//import org.scalamock.scalatest.MockFactory
//import org.scalatest.{FunSuite, Matchers}
//
///**
//  * @author Romesh Selvan
//  */
//class ProjectRepoTest extends FunSuite with Matchers with MockFactory {
//
//  val TABLENAME = "table"
//
//  val dynamoDB = stub[DynamoDB]
//  val table = stub[Table]
//  val projectRepo = new ProjectRepo(dynamoDB, ProjectDynamoConverter)
//
//  test("When Project list contain two items then the list returned should be ordered") {
//    val itemCollection = stub[ItemCollection[ScanOutcome]]
//    (table.scan (_:ScanSpec)).when(*).returns(itemCollection)
//  }
//
//  test("When Saving a Project with Order Number 0 and the Projects retrieved Is Empty then jus add as first in order"){
//
//  }
//
//  def before = (dynamoDB.getTable _).when("TABLENAME").returns(table)
//}
