package controllers

import controllers.util.ProjectValidationWrapper
import defaults.{ButtonTypes, Roles}
import defaults.Roles.Role
import domain.{GalleryLink, Project, User}
import dynamoDB.tableFields.ProjectsFieldNames
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers}
import play.api.mvc.Result
import play.api.test.{FakeHeaders, FakeRequest}
import repositories.ProjectRepo
import security.JWTUtil
import play.api.test.Helpers._

import scala.concurrent.Future

/**
  * @author Romesh Selvan
  */
class ProjectControllerTest extends FunSuite with Matchers with MockFactory with BeforeAndAfterAll {

  val PROJECT_ID = "projectId"
  val PROJECT_TITLE = "projectTitle"
  val TITLE_IMAGE_LINK = "titleImageLink"
  val BUTTON_TYPES = List(ButtonTypes.GITHUB, ButtonTypes.DIRECT_LINK)
  val GITHUB_LINK = "giHublink"
  val VIDEO_LINK = "videoLink"
  val GALLERY_LINKS = List(GalleryLink("url1"), GalleryLink("url2"))
  val DIRECT_LINK = "link"
  val ORDER = 2

  val tableName = "mypage-projects"
  val repo = stub[ProjectRepo]
  val controller = new ProjectController(repo, ProjectValidationWrapper)

  override def beforeAll = {
    sys.props.put("JWTSECRET", "secret")
    sys.props.put("AWS_ACCESS_KEY_ID", "access")
    sys.props.put("AWS_SECRET_ACCESS_KEY", "aws")
  }

  test("Project Controller must return a 404 response when email is not found") {
    (repo.findOne (_:String, _:String)(_:String)).when(ProjectsFieldNames.PROJECT_ID, PROJECT_ID, tableName).returns(None)
    val result: Future[Result] = controller.getProject(PROJECT_ID).apply(getRequest(Roles.MYPAGE_APP))
    val statusCode = status(result)
    val content = contentAsString(result)

    statusCode should be (404)
    content should be ("Project not found")
  }

  test("Project Controller must return a Success response when email is found with correct json") {
    (repo.findOne (_:String, _:String)(_:String)).when(ProjectsFieldNames.PROJECT_ID, PROJECT_ID, tableName).returns(Some(defaultProjecct))
    val result: Future[Result] = controller.getProject(PROJECT_ID).apply(getRequest(Roles.MYPAGE_APP))
    val statusCode = status(result)
    val json = contentAsJson(result)

    statusCode should be (200)
    json.toString() should be (expectedJson)
  }

  test("Only MyPage User and Admin user are allowed to the Use the Get API") {
    val result: Future[Result] = controller.getProject(PROJECT_ID).apply(getRequest(Roles.ROMCHARM_APP))
    val statusCode = status(result)

    statusCode should be (403)
  }

  test("Project Controller must return all projects when the projects are present in the DB") {
    (repo.findAll (_:String)).when(tableName).returns(List(defaultProjecct, defaultProjecct))
    val result: Future[Result] = controller.getAllProjects.apply(getRequest(Roles.MYPAGE_APP))
    val statusCode = status(result)
    val json = contentAsJson(result)
    val jsonList = s"""[$expectedJson,$expectedJson]"""

    statusCode should be (200)
    json.toString() should be (jsonList)
  }

  test("Project Controller must return empty project list when no projects are present in the DB") {
    (repo.findAll (_:String)).when(tableName).returns(List())
    val result: Future[Result] = controller.getAllProjects.apply(getRequest(Roles.MYPAGE_APP))
    val statusCode = status(result)
    val json = contentAsJson(result)
    val jsonList = s"""[]"""

    statusCode should be (200)
    json.toString() should be (jsonList)
  }

  test("Only MyPage User and Admin user are allowed to the Use the Get All Projects API") {
    val result: Future[Result] = controller.getAllProjects.apply(getRequest(Roles.ROMCHARM_APP))
    val statusCode = status(result)

    statusCode should be (403)
  }

  def getRequest(role : Role) = {
    val token = JWTUtil.generateToken(User("romesh", "password", Roles.getRoleString(role)))
    FakeRequest("GET", "/", FakeHeaders(List("Authorization" -> token).asInstanceOf[Seq[(String, String)]]), null)
  }

  def defaultProjecct = Project(PROJECT_ID, PROJECT_TITLE,
    Option(TITLE_IMAGE_LINK),
    Option(BUTTON_TYPES),
    Option(GITHUB_LINK),
    Option(VIDEO_LINK),
    Option(GALLERY_LINKS),
    Option(DIRECT_LINK),
    Option(ORDER)
  )

  def expectedJson =
    s"""{"projectId":"$PROJECT_ID","projectTitle":"$PROJECT_TITLE","titleImageLink":"$TITLE_IMAGE_LINK","buttonTypes":["GITHUB","DIRECT_LINK"],"githubLink":"$GITHUB_LINK","videoLink":"$VIDEO_LINK","galleryLinks":[{"url":"url1"},{"url":"url2"}],"directLink":"$DIRECT_LINK","order":$ORDER}"""

}
