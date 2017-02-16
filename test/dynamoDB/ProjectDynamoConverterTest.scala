package dynamoDB

import com.amazonaws.services.dynamodbv2.document.Item
import domain.{GalleryLink, Project}
import org.scalatestplus.play.PlaySpec
import collection.JavaConverters._

/**
  * @author Romesh Selvan
  */
class ProjectDynamoConverterTest extends PlaySpec {

  val PROJECT_ID = "starrage"
  val PROJECT_TITLE = "Star Rage"
  val TITLE_IMAGE_LINK = "linkTitle"
  val BUTTON_TYPES = List("GITHUB", "DIRECT_LINK")
  val GITHUB_LINK = "linkGit"
  val VIDEO_LINK = "videoLink"
  val GALLERY_LINK = List(GalleryLink("url1"))
  val DIRECT_LINK = "directLink"
  val ORDER = 1

  "Converter" must {
    "convert from Project object to DynamoDB Item object" in {
      val project = defaultProject
      val expected = defaultItem
      val result = ProjectDynamoConverter(project)
      result mustBe expected
    }

    "convert from DynamDB object back to Project object" in {
      val item = defaultItem
      val expected = defaultProject
      val result = ProjectDynamoConverter(item)
      result mustBe expected
    }
  }

  def defaultProject = Project(PROJECT_ID, PROJECT_TITLE, TITLE_IMAGE_LINK, BUTTON_TYPES, GITHUB_LINK, VIDEO_LINK, GALLERY_LINK, DIRECT_LINK, ORDER)

  def defaultItem =
    new Item().withString("projectId", PROJECT_ID)
                .withString("projectTitle", PROJECT_TITLE)
                .withString("titleImageLink", TITLE_IMAGE_LINK)
                .withList("buttonTypes", BUTTON_TYPES.asJava)
                .withString("githubLink", GITHUB_LINK)
                .withString("videoLink", VIDEO_LINK)
                .withList("gallerylinks", GALLERY_LINK.asJava)
                .withString("directLink", DIRECT_LINK)
                .withInt("order", ORDER)
}
