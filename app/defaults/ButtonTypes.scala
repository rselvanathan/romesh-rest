package defaults

import play.api.libs.json._

/**
  * @author Romesh Selvan
  */
object ButtonTypes {

  trait ButtonType

  object GITHUB extends ButtonType
  object GALLERY extends ButtonType
  object VIDEO extends ButtonType
  object DIRECT_LINK extends ButtonType

  def buttonTypeToString(buttonType: ButtonType) = buttonType match {
    case GITHUB => "GITHUB"
    case GALLERY => "GALLERY"
    case VIDEO => "VIDEO"
    case DIRECT_LINK => "DIRECT_LINK"
  }

  def stringToButtonType(value : String) = value match {
    case "GITHUB" => GITHUB
    case "GALLERY" => GALLERY
    case "VIDEO" => VIDEO
    case "DIRECT_LINK" => DIRECT_LINK
  }

  implicit val jsonFormat = new Format[ButtonType] {
    override def writes(o: ButtonType): JsValue = JsString(ButtonTypes.buttonTypeToString(o))

    override def reads(json: JsValue): JsResult[ButtonType] = JsSuccess(ButtonTypes.stringToButtonType(json.as[JsString].value))
  }
}
