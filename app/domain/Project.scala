package domain

import defaults.ButtonTypes.ButtonType
import play.api.libs.json.Json

/**
  * @author Romesh Selvan
  */
case class Project(projectId : String,
                   projectTitle : String,
                   titleImageLink : Option[String],
                   buttonTypes : Option[Seq[ButtonType]],
                   githubLink : Option[String],
                   videoLink : Option[String],
                   galleryLinks: Option[Seq[GalleryLink]],
                   directLink : Option[String],
                   order : Option[Int])

object Project {
  implicit val reader = Json.reads[Project]
  implicit val writer = Json.writes[Project]
}
