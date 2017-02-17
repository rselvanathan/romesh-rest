package dynamoDB.converters

import com.amazonaws.services.dynamodbv2.document.Item
import domain.{GalleryLink, Project}
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import org.scalatestplus.play.PlaySpec

import scala.collection.JavaConverters._

/**
  * @author Romesh Selvan
  */
class ProjectDynamoConverterTest extends PlaySpec {

  implicit val formats = Serialization.formats(NoTypeHints)

  val galleryLinks = List(GalleryLink("url1"))

  val PROJECT_ID = "starrage"
  val PROJECT_TITLE = "Star Rage"
  val TITLE_IMAGE_LINK = "linkTitle"
  val BUTTON_TYPES = List("GITHUB", "DIRECT_LINK")
  val GITHUB_LINK = "linkGit"
  val VIDEO_LINK = "videoLink"
  val GALLERY_LINKS_STRING = write(galleryLinks)
  val DIRECT_LINK = "directLink"
  val ORDER = 1

  "Converter" must {
    "convert from Project object to DynamoDB Item object" in {
      val project = defaultProject(galleryLinks)
      val expected = defaultItem(GALLERY_LINKS_STRING)
      val result = ProjectDynamoConverter(project)
      result mustBe expected
    }

    "convert from DynamDB object back to Project object" in {
      val item = defaultItem(GALLERY_LINKS_STRING)
      val expected = defaultProject(galleryLinks)
      val result = ProjectDynamoConverter(item)
      result mustBe expected
    }

    "convert from DynamDB object with null Gallery Links back to Project object with null Gallery Links" in {
      val item = defaultItem(null)
      val expected = defaultProject(null)
      val result = ProjectDynamoConverter(item)
      result mustBe expected
    }
  }

  private def defaultProject(galleryLinks : Seq[GalleryLink]) = Project(PROJECT_ID, PROJECT_TITLE, TITLE_IMAGE_LINK, BUTTON_TYPES, GITHUB_LINK, VIDEO_LINK, galleryLinks, DIRECT_LINK, ORDER)

  private def defaultItem(galleryLinks : String) = {
    if(galleryLinks != null) {
      new Item().withString("projectId", PROJECT_ID)
        .withString("projectTitle", PROJECT_TITLE)
        .withString("titleImageLink", TITLE_IMAGE_LINK)
        .withList("buttonTypes", BUTTON_TYPES.asJava)
        .withString("githubLink", GITHUB_LINK)
        .withString("videoLink", VIDEO_LINK)
        .withString("galleryLinks", galleryLinks)
        .withString("directLink", DIRECT_LINK)
        .withInt("order", ORDER)
    } else {
      new Item().withString("projectId", PROJECT_ID)
        .withString("projectTitle", PROJECT_TITLE)
        .withString("titleImageLink", TITLE_IMAGE_LINK)
        .withList("buttonTypes", BUTTON_TYPES.asJava)
        .withString("githubLink", GITHUB_LINK)
        .withString("videoLink", VIDEO_LINK)
        .withString("directLink", DIRECT_LINK)
        .withInt("order", ORDER)
    }
  }
}
