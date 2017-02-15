package domain

/**
  * @author Romesh Selvan
  */
case class GalleryLink(url : String)

case class Project(projectId : String,
                   projectTitle : String,
                   titleImageLink : String,
                   buttonTypes : Seq[String],
                   githubLink : String,
                   videoLink : String,
                   galleryLinks: Seq[GalleryLink],
                   directLink : String,
                   order : Int)
