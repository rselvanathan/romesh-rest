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
    var item = new Item().withString(PROJECT_ID, project.projectId)
      .withString(PROJECT_TITLE, project.projectTitle)
      .withInt(ORDER, project.order.get)
    if(project.galleryLinks.isDefined) item = item.withString(GALLERY_LINKS, write(project.galleryLinks))
    if(project.buttonTypes.isDefined) item = item.withList(BUTTON_TYPES, project.buttonTypes.get.map(ButtonTypes.buttonTypeToString).asJava)
    if(project.titleImageLink.isDefined) item = item.withString(TITLE_IMAGE_LINK, project.titleImageLink.get)
    if(project.githubLink.isDefined) item = item.withString(GITHUB_LINK, project.githubLink.get)
    if(project.videoLink.isDefined) item = item.withString(VIDEO_LINK, project.videoLink.get)
    if(project.directLink.isDefined) item = item.withString(DIRECT_LINK, project.directLink.get)
    item
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
