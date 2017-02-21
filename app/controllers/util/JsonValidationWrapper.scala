package controllers.util

import akka.util.ByteString
import domain.{Family, Login, User}
import play.api.http.HttpEntity
import play.api.libs.json.{JsError, JsResult, JsSuccess, JsValue}
import play.api.mvc.{ResponseHeader, Result}

/**
  * @author Romesh Selvan
  */
trait JsonValidationWrapper[T] {

  /**
    * Will take a JsValue and a function as a parameter. The function should process the JSON Object once the validation
    * succeeds. Otherwise a default Bad Request will be returned
    * @param jsValue - the jsValue to validate
    * @param successFunction - the function to apply to the validated jsValue
    * @return Result - A result to return back to the REST user
    */
  def apply(successFunction:T => Result)(implicit jsValue : Option[JsValue]) : Result = {
    if(jsValue.get == null) badRequest("No Json body was found")
    else {
      validate match {
        case success : JsSuccess[T] => successFunction(success.get)
        case JsError(error) => badRequest("JSON Object is incorrect")
      }
    }
  }

  private def badRequest(text : String) : Result = Result(
    header = ResponseHeader(400, Map.empty),
    body = HttpEntity.Strict(ByteString(text), Some("text/plain"))
  )

  protected def validate(implicit jsValue: Option[JsValue]) : JsResult[T]
}


object FamilyValidationWrapper extends JsonValidationWrapper[Family] {
  override protected def validate(implicit jsValue: Option[JsValue]): JsResult[Family] = jsValue.get.validate[Family]
}

object LoginValidationWrapper extends JsonValidationWrapper[Login] {
  override protected def validate(implicit jsValue: Option[JsValue]): JsResult[Login] = jsValue.get.validate[Login]
}

object UserValidationWrapper extends JsonValidationWrapper[User] {
  override protected def validate(implicit jsValue: Option[JsValue]): JsResult[User] = jsValue.get.validate[User]
}
