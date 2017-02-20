package controllers.util
import domain.{Login, User}
import play.api.libs.json.{JsError, JsSuccess, JsValue}
import play.api.mvc.Result

/**
  * @author Romesh Selvan
  */
object UserValidationWrapper extends JsonValidationWrapper{

  override def validate[T](successFunction: (T) => Result)(implicit jsValue: Option[JsValue]): Result = {
    // The type has to be specified for validate to function. Seems to not like the "T" template.
    // Hence the reason for creating this Domain specific class
    jsValue.get.validate[Login] match {
      case success: JsSuccess[T] => successFunction(success.get)
      case JsError(error) => badRequest("JSON Object is incorrect")
    }
  }
}
