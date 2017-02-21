package controllers.util
import domain.Family
import play.api.libs.json.{JsResult, JsValue}

/**
  * @author Romesh Selvan
  */
object FamilyValidationWrapper extends JsonValidationWrapper {

  override protected def validateJson(implicit jsValue: Option[JsValue]): JsResult[Any] = {
    jsValue.get.validate[Family]
  }
}
