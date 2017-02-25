package dynamoDB.converters

import com.amazonaws.services.dynamodbv2.document.Item
import defaults.ButtonTypes
import defaults.ButtonTypes.ButtonType
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
      .withString(TITLE_IMAGE_LINK, project.titleImageLink.get)
      .withList(BUTTON_TYPES, project.buttonTypes.getOrElse(List()).map(ButtonTypes.buttonTypeToString).asJava)
      .withString(GITHUB_LINK, project.githubLink.get)
      .withString(VIDEO_LINK, project.videoLink.get)
      .withString(GALLERY_LINKS, write(project.galleryLinks))
      .withString(DIRECT_LINK, project.directLink.get)
      .withInt(ORDER, project.order.get)
  }

  def apply(item: Item) : Project = {
    Project(item.getString(PROJECT_ID),
      item.getString(PROJECT_TITLE),
      Option(item.getString(TITLE_IMAGE_LINK)),
      getButtonTypes(item),
      Option(item.getString(GITHUB_LINK)),
      Option(item.getString(VIDEO_LINK)),
      getGalleryLinks(item.getString(GALLERY_LINKS)),
      Option(item.getString(DIRECT_LINK)),
      Option(item.getInt(ORDER)))
  }

  private def getGalleryLinks(json : String) : Option[Seq[GalleryLink]] = {
    if(json == null) return None
    val jsonValue : JValue = parse(json)
    val array = jsonValue.asInstanceOf[JArray]
    Some(array.arr.map(_.extract[GalleryLink]))
  }

  private def getButtonTypes(item : Item) : Option[Seq[ButtonType]] = if(item.getList(BUTTON_TYPES) == null) None else Some(item.getList(BUTTON_TYPES).asScala.map(ButtonTypes.stringToButtonType))


  private def getTitleImage(titleImage : String) = Option(titleImage)
}
