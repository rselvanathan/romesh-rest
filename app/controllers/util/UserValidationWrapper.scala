package controllers.util
import domain.Login
import play.api.libs.json.{JsResult, JsValue}

/**
  * @author Romesh Selvan
  */
object UserValidationWrapper extends JsonValidationWrapper{

  override protected def validateJson(implicit jsValue: Option[JsValue]): JsResult[Any] = {
    jsValue.get.validate[Login]
  }
}
