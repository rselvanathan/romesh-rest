package domain

import play.api.libs.json.Json

/**
  * @author Romesh Selvan
  */
case class GalleryLink(url : String)

object GalleryLink {
  implicit val reads = Json.format[GalleryLink]
  implicit val writer = Json.writes[Project]
}
