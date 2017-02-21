package dynamoDB.converters

import com.amazonaws.services.dynamodbv2.document.Item
import domain.{GalleryLink, Project}
import dynamoDB.tableFields.ProjectsFieldNames._
import org.json4s.JsonAST.JArray
import org.json4s.native.JsonParser._
import org.json4s.native.Serialization
import org.json4s.native.Serialization._
import org.json4s.{DefaultFormats, JValue, NoTypeHints}

import scala.collection.JavaConverters._

/**
  * @author Romesh Selvan
  */
object ProjectDynamoConverter extends DynamoDBConverter[Project]{

  implicit val formats = Serialization.formats(NoTypeHints)
  implicit val extractFormats = DefaultFormats

  def apply(project : Project) : Item = {
    new Item().withString(PROJECT_ID, project.projectId)
      .withString(PROJECT_TITLE, project.projectTitle)
      .withString(TITLE_IMAGE_LINK, project.titleImageLink)
      .withList(BUTTON_TYPES, project.buttonTypes.asJava)
      .withString(GITHUB_LINK, project.githubLink)
      .withString(VIDEO_LINK, project.videoLink)
      .withString(GALLERY_LINKS, write(project.galleryLinks))
      .withString(DIRECT_LINK, project.directLink)
      .withInt(ORDER, project.order)
  }

  def apply(item: Item) : Project = {
    Project(item.getString(PROJECT_ID),
      item.getString(PROJECT_TITLE),
      item.getString(TITLE_IMAGE_LINK),
      item.getList(BUTTON_TYPES).asScala,
      item.getString(GITHUB_LINK),
      item.getString(VIDEO_LINK),
      getGalleryLinks(item.getString(GALLERY_LINKS)),
      item.getString(DIRECT_LINK),
      item.getInt(ORDER))
  }

  private def getGalleryLinks(json : String) : Seq[GalleryLink] = {
    if(json == null) return null
    val jsonValue : JValue = parse(json)
    val array = jsonValue.asInstanceOf[JArray]
    array.arr.map(_.extract[GalleryLink])
  }
}
