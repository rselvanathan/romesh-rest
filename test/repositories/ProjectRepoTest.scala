package repositories

import com.amazonaws.services.dynamodbv2.document._
import com.amazonaws.services.dynamodbv2.document.internal.IteratorSupport
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec
import defaults.TableNames
import domain.Project
import dynamoDB.converters.ProjectDynamoConverter
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FunSuite, Matchers}
import org.mockito.Mockito._
import org.mockito.Matchers._

/**
  * @author Romesh Selvan
  */

class ProjectRepoTest extends FunSuite with Matchers with MockitoSugar {

  implicit val TABLENAME = TableNames.MYPAGE_PROJECTS

  val dynamoDB = mock[DynamoDB]
  val table = mock[Table]
  val projectRepo = new ProjectRepo(dynamoDB, ProjectDynamoConverter)

  test("When Project list contain two items then the list returned should be ordered") {
    before
    val iteratorSupport = mock[IteratorSupport[Item, ScanOutcome]]
    val itemCollection = mock[ItemCollection[ScanOutcome]]

    when(iteratorSupport.hasNext).thenReturn(true).thenReturn(true).thenReturn(false)
    when(iteratorSupport.next).thenReturn(defaultItem(2)).thenReturn(defaultItem(1))
    when(itemCollection.iterator).thenReturn(iteratorSupport)
    when(table.scan(any(classOf[ScanSpec]))).thenReturn(itemCollection)

    val projectList = projectRepo.findAll

    projectList should be (List(defaultProject(1), defaultProject(2)))
  }

  test("When Saving a Project with Order Number 0 and the Projects retrieved Is Empty then jus add as first in order"){
    before
    val iteratorSupport = mock[IteratorSupport[Item, ScanOutcome]]
    val itemCollection = mock[ItemCollection[ScanOutcome]]

    when(iteratorSupport.hasNext).thenReturn(false)
    when(itemCollection.iterator).thenReturn(iteratorSupport)
    when(table.scan(any(classOf[ScanSpec]))).thenReturn(itemCollection)

    projectRepo.save(getProject("projectToSave", 0))

    verify(table).putItem(getItem("projectToSave", 1))
  }

  test("When Saving a Project with Order Number 5 and the Projects retrieved Is Empty then jus add as first in order"){
    before
    val iteratorSupport = mock[IteratorSupport[Item, ScanOutcome]]
    val itemCollection = mock[ItemCollection[ScanOutcome]]

    when(iteratorSupport.hasNext).thenReturn(false)
    when(itemCollection.iterator).thenReturn(iteratorSupport)
    when(table.scan(any(classOf[ScanSpec]))).thenReturn(itemCollection)

    projectRepo.save(getProject("projectToSave", 5))

    verify(table).putItem(getItem("projectToSave", 1))
  }

  test("When Saving a Project with Order Number 0 and the Projects retrieved have one project then add it after that project"){
    before
    val iteratorSupport = mock[IteratorSupport[Item, ScanOutcome]]
    val itemCollection = mock[ItemCollection[ScanOutcome]]

    when(iteratorSupport.hasNext).thenReturn(true, false)
    when(iteratorSupport.next()).thenReturn(defaultItem(1))
    when(itemCollection.iterator).thenReturn(iteratorSupport)
    when(table.scan(any(classOf[ScanSpec]))).thenReturn(itemCollection)

    projectRepo.save(getProject("projectToSave", 0))

    verify(table).putItem(getItem("projectToSave", 2))
  }

  test("When Saving a Project with Order Number 10 and the Projects retrieved have one project then add it after that project"){
    before
    val iteratorSupport = mock[IteratorSupport[Item, ScanOutcome]]
    val itemCollection = mock[ItemCollection[ScanOutcome]]

    when(iteratorSupport.hasNext).thenReturn(true, false)
    when(iteratorSupport.next()).thenReturn(defaultItem(1))
    when(itemCollection.iterator).thenReturn(iteratorSupport)
    when(table.scan(any(classOf[ScanSpec]))).thenReturn(itemCollection)

    projectRepo.save(getProject("projectToSave", 10))

    verify(table).putItem(getItem("projectToSave", 2))
  }

  test("When Saving a new project with Order number that already exists then it should replace order of previous, where " +
       "previous and all other projects after it in the list, will move one position to the right") {
    before
    val iteratorSupport = mock[IteratorSupport[Item, ScanOutcome]]
    val itemCollection = mock[ItemCollection[ScanOutcome]]

    when(iteratorSupport.hasNext).thenReturn(true, true, true, false)
    when(iteratorSupport.next()).thenReturn(getItem("one", 1), getItem("two", 2), getItem("three", 3))
    when(itemCollection.iterator).thenReturn(iteratorSupport)
    when(table.scan(any(classOf[ScanSpec]))).thenReturn(itemCollection)

    projectRepo.save(getProject("projectToSave", 2))

    verify(table).putItem(getItem("projectToSave", 2))
    verify(table).putItem(getItem("two", 3))
    verify(table).putItem(getItem("three", 4))
  }

  test("When Saving a old project with the same order number as before, then it should just overwrite the previous entry") {
    before
    val iteratorSupport = mock[IteratorSupport[Item, ScanOutcome]]
    val itemCollection = mock[ItemCollection[ScanOutcome]]

    when(iteratorSupport.hasNext).thenReturn(true, true, true, false)
    when(iteratorSupport.next()).thenReturn(getItem("one", 1), getItem("projectToReplace", 2), getItem("three", 3))
    when(itemCollection.iterator).thenReturn(iteratorSupport)
    when(table.scan(any(classOf[ScanSpec]))).thenReturn(itemCollection)

    projectRepo.save(getProject("projectToReplace", 2))

    verify(table).putItem(getItem("projectToReplace", 2))
    verify(table, never()).putItem(getItem("three", 4))
  }

  test("When Saving a old project with the no order number, then it should just overwrite the previous entry") {
    before
    val iteratorSupport = mock[IteratorSupport[Item, ScanOutcome]]
    val itemCollection = mock[ItemCollection[ScanOutcome]]

    when(iteratorSupport.hasNext).thenReturn(true, true, true, false)
    when(iteratorSupport.next()).thenReturn(getItem("one", 1), getItem("projectToReplace", 2), getItem("three", 3))
    when(itemCollection.iterator).thenReturn(iteratorSupport)
    when(table.scan(any(classOf[ScanSpec]))).thenReturn(itemCollection)

    projectRepo.save(getProject("projectToReplace", 0))

    verify(table).putItem(getItem("projectToReplace", 2))
    verify(table, never()).putItem(getItem("three", 4))
  }

  test("When Saving a old project with a order number beyond the size of the list, then it should be moved to the end, " +
       "while the rest of the projects are moved one position to the left") {
    before
    val iteratorSupport = mock[IteratorSupport[Item, ScanOutcome]]
    val itemCollection = mock[ItemCollection[ScanOutcome]]

    when(iteratorSupport.hasNext).thenReturn(true, true, true, false)
    when(iteratorSupport.next()).thenReturn(getItem("projectToReplace", 1), getItem("two", 2), getItem("three", 3))
    when(itemCollection.iterator).thenReturn(iteratorSupport)
    when(table.scan(any(classOf[ScanSpec]))).thenReturn(itemCollection)

    projectRepo.save(getProject("projectToReplace", 5))

    verify(table).putItem(getItem("projectToReplace", 3))
    verify(table).putItem(getItem("two", 1))
    verify(table).putItem(getItem("three", 2))
  }

  test("When Saving a old project with a new order number that is towards the end of the list, then it should move every project " +
       "to the right of it including the project whose order is to be replaced, by one position to the left") {
    before
    val iteratorSupport = mock[IteratorSupport[Item, ScanOutcome]]
    val itemCollection = mock[ItemCollection[ScanOutcome]]

    when(iteratorSupport.hasNext).thenReturn(true, true, true, true, true, true,false)
    when(iteratorSupport.next()).thenReturn(getItem("one", 1),
      getItem("projectToMove", 2), getItem("three", 3), getItem("four", 4), getItem("five", 5), getItem("six", 6))
    when(itemCollection.iterator).thenReturn(iteratorSupport)
    when(table.scan(any(classOf[ScanSpec]))).thenReturn(itemCollection)

    projectRepo.save(getProject("projectToMove", 5))

    verify(table).putItem(getItem("projectToMove", 5))
    verify(table).putItem(getItem("three", 2))
    verify(table).putItem(getItem("four", 3))
    verify(table).putItem(getItem("five", 4))

    verify(table, never).putItem(getItem("six", 5))
    verify(table, never).putItem(getItem("six", 6))
    verify(table, never).putItem(getItem("six", 7))
  }

  test("When Saving a old project with a new order number that is towards the beginning of the list, then it should move every project " +
    "to the left of it including the project whose order is to be replaced, by one position to the right") {
    before
    val iteratorSupport = mock[IteratorSupport[Item, ScanOutcome]]
    val itemCollection = mock[ItemCollection[ScanOutcome]]

    when(iteratorSupport.hasNext).thenReturn(true, true, true, true, true, true,false)
    when(iteratorSupport.next()).thenReturn(getItem("one", 1),
      getItem("two", 2), getItem("three", 3), getItem("four", 4), getItem("projectToMove", 5), getItem("six", 6))
    when(itemCollection.iterator).thenReturn(iteratorSupport)
    when(table.scan(any(classOf[ScanSpec]))).thenReturn(itemCollection)

    projectRepo.save(getProject("projectToMove", 2))

    verify(table).putItem(getItem("projectToMove", 2))
    verify(table).putItem(getItem("two", 3))
    verify(table).putItem(getItem("three", 4))
    verify(table).putItem(getItem("four", 5))

    verify(table, never).putItem(getItem("one", 2))
    verify(table, never).putItem(getItem("one", 1))
  }

//  def before = (dynamoDB.getTable _).when(TableNames.getTableName(TABLENAME)).returns(table)
  def before = {
    reset(table)
    when(dynamoDB.getTable(TableNames.getTableName(TABLENAME))).thenReturn(table)
  }

  def defaultItem(order : Int) = getItem("ProjectId", order)
  def defaultProject(order : Int) = getProject("ProjectId", order)

  def getProject(projectID : String, order : Int) = Project(projectID, "ProjectTitle", None, None, None, None, None, None, Option(order))
  def getItem(projectId : String, order : Int) = new Item().withString("projectId", projectId).withString("projectTitle", "ProjectTitle").withInt("order", order)
}
