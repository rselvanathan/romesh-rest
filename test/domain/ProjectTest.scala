package domain

import defaults.ButtonTypes
import org.json4s.DefaultFormats
import org.json4s.native.Serialization.write
import org.scalatest.{FunSuite, Matchers}
import play.api.libs.json.Json

/**
  * @author Romesh Selvan
  */
class ProjectTest extends FunSuite with Matchers {

  implicit val formats = DefaultFormats

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


  def projectJson =
    """
      |{
      |  "buttonTypes": [
      |    "GITHUB"
      |  ],
      |  "directLink": "string",
      |  "galleryLinks": [
      |    {
      |      "url": "string"
      |    }
      |  ],
      |  "githubLink": "string",
      |  "order": 0,
      |  "projectId": "string",
      |  "projectTitle": "string",
      |  "titleImageLink": "string",
      |  "videoLink": "string"
      |}
    """.stripMargin

  test("Convert a minimum typical Project JSON into Project object") {
    def projectJson =
      s"""
        |{
        |  "projectId":"$PROJECT_ID",
        |  "projectTitle":"$PROJECT_TITLE"
        |}
      """.stripMargin

    val json = Json.parse(projectJson)
    val result = json.validate[Project]
    result.isError should be (false)
  }

  test("No ProjectId should fail validation") {
    def projectJson =
      s"""
         |{
         |  "projectTitle":"$PROJECT_TITLE"
         |}
      """.stripMargin
    assertProjectJson(projectJson)
  }

  test("No Project Title should fail validation") {
    def projectJson =
      s"""
         |{
         |  "projectId":"$PROJECT_ID"
         |}
      """.stripMargin
    assertProjectJson(projectJson)
  }

  test("Convert a full Project Json correctly") {
    def projectJson =
      s"""
        |{
        |  "buttonTypes": [
        |    "GITHUB",
        |    "DIRECT_LINK"
        |  ],
        |  "directLink": "$DIRECT_LINK",
        |  "galleryLinks": [
        |    {
        |      "url": "url1"
        |    }
        |  ],
        |  "githubLink": "$GITHUB_LINK",
        |  "order": $ORDER,
        |  "projectId": "$PROJECT_ID",
        |  "projectTitle": "$PROJECT_TITLE",
        |  "titleImageLink": "$TITLE_IMAGE_LINK",
        |  "videoLink": "$VIDEO_LINK"
        |}
      """.stripMargin

    val expectedProject =
      Project(PROJECT_ID, PROJECT_TITLE,
        Option(TITLE_IMAGE_LINK),
        Option(BUTTON_TYPES),
        Option(GITHUB_LINK),
        Option(VIDEO_LINK),
        Option(galleryLinks),
        Option(DIRECT_LINK),
        Option(ORDER)
      )

    val json = Json.parse(projectJson)
    val result = json.validate[Project]
    val resultObject = json.as[Project]
    result.isError should be (false)
    resultObject should be (expectedProject)
  }

  test("Convert a full Project object to full json") {
    val project =
      Project(PROJECT_ID, PROJECT_TITLE,
        Option(TITLE_IMAGE_LINK),
        Option(BUTTON_TYPES),
        Option(GITHUB_LINK),
        Option(VIDEO_LINK),
        Option(galleryLinks),
        Option(DIRECT_LINK),
        Option(ORDER)
      )

    def expectedJson =
      s"""{"projectId":"$PROJECT_ID","projectTitle":"$PROJECT_TITLE","titleImageLink":"$TITLE_IMAGE_LINK","buttonTypes":["GITHUB","DIRECT_LINK"],"githubLink":"$GITHUB_LINK","videoLink":"$VIDEO_LINK","galleryLinks":[{"url":"url1"}],"directLink":"$DIRECT_LINK","order":$ORDER}"""

    val value = Json.toJson(project)
    value.toString() should be (expectedJson)
  }

  private def assertProjectJson(jsonString : String) = {
    val json = Json.parse(jsonString)
    val result = json.validate[Project]
    result.isError should be (true)
  }
}
