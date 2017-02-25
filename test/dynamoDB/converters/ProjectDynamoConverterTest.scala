package dynamoDB.converters

import com.amazonaws.services.dynamodbv2.document.Item
import defaults.ButtonTypes
import domain.{GalleryLink, Project}
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import org.scalatest.{FunSuite, Matchers}

import scala.collection.JavaConverters._

/**
  * @author Romesh Selvan
  */
class ProjectDynamoConverterTest extends FunSuite with Matchers {

  implicit val formats = Serialization.formats(NoTypeHints)

  val galleryLinks = List(GalleryLink("url1"))

  val PROJECT_ID = "starrage"
  val PROJECT_TITLE = "Star Rage"
  val TITLE_IMAGE_LINK = "linkTitle"
  val BUTTON_TYPES = List(ButtonTypes.GITHUB, ButtonTypes.DIRECT_LINK)
  val GITHUB_LINK = "linkGit"
  val VIDEO_LINK = "videoLink"
  val GALLERY_LINKS_STRING = write(galleryLinks)
  val DIRECT_LINK = "directLink"
  val ORDER = 1

  test("Converter must convert from Project object to DynamoDB Item object") {
      val project = defaultProject(galleryLinks)
      val expected = defaultItem(GALLERY_LINKS_STRING)
      val result = ProjectDynamoConverter(project)
      result should be (expected)
    }

  test("Converter must convert from DynamDB object back to Project object") {
      val item = defaultItem(GALLERY_LINKS_STRING)
      val expected = defaultProject(galleryLinks)
      val result = ProjectDynamoConverter(item)
      result should be (expected)
    }

  test("Converter must convert from DynamDB object with null Gallery Links back to Project object with null Gallery Links") {
      val item = defaultItem(null)
      val expected = defaultProject(null)
      val result = ProjectDynamoConverter(item)
      result should be (expected)
    }

  test("When None of the optional fields are set in the Dynamo Table return None's for those fields") {
    val exprectProject = Project(PROJECT_ID, PROJECT_TITLE, None, None, None, None, None, None, Some(0))
    val item = new Item().withString("projectId", PROJECT_ID)
      .withString("projectTitle", PROJECT_TITLE)
      .withInt("order", 0)

    val result = ProjectDynamoConverter(item)
    result should be (exprectProject)
  }

  private def defaultProject(galleryLinks : Seq[GalleryLink]) =
    Project(PROJECT_ID,
      PROJECT_TITLE,
      Option(TITLE_IMAGE_LINK),
      Option(BUTTON_TYPES),
      Option(GITHUB_LINK),
      Option(VIDEO_LINK),
      Option(galleryLinks),
      Option(DIRECT_LINK),
      Option(ORDER))

  private def defaultItem(galleryLinks : String) = {
    if(galleryLinks != null) {
      new Item().withString("projectId", PROJECT_ID)
        .withString("projectTitle", PROJECT_TITLE)
        .withString("titleImageLink", TITLE_IMAGE_LINK)
        .withList("buttonTypes", BUTTON_TYPES.map(ButtonTypes.buttonTypeToString).asJava)
        .withString("githubLink", GITHUB_LINK)
        .withString("videoLink", VIDEO_LINK)
        .withString("galleryLinks", galleryLinks)
        .withString("directLink", DIRECT_LINK)
        .withInt("order", ORDER)
    } else {
      new Item().withString("projectId", PROJECT_ID)
        .withString("projectTitle", PROJECT_TITLE)
        .withString("titleImageLink", TITLE_IMAGE_LINK)
        .withList("buttonTypes", BUTTON_TYPES.map(ButtonTypes.buttonTypeToString).asJava)
        .withString("githubLink", GITHUB_LINK)
        .withString("videoLink", VIDEO_LINK)
        .withString("directLink", DIRECT_LINK)
        .withInt("order", ORDER)
    }
  }
}
