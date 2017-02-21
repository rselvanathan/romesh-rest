package controllers.util

import akka.util.ByteString
import play.api.http.HttpEntity
import play.api.libs.json.{JsError, JsResult, JsSuccess, JsValue}
import play.api.mvc.{ResponseHeader, Result}

/**
  * @author Romesh Selvan
  */
trait JsonValidationWrapper {

  /**
    * Will take a JsValue and a function as a parameter. The function should process the JSON Object once the validation
    * succeeds. Otherwise a default Bad Request will be returned
    * @param jsValue - the jsValue to validate
    * @param successFunction - the function to apply to the validated jsValue
    * @tparam T - Domain type of the json value
    * @return Result - A result to return back to the REST user
    */
  def apply[T](successFunction:T => Result)(implicit jsValue : Option[JsValue]) : Result = {
    if(jsValue.get == null) badRequest("No Json body was found")
    else {
      validateJson match {
        case success : JsSuccess[T] => successFunction(success.get)
        case JsError(error) => badRequest("JSON Object is incorrect")
      }
    }
  }

  protected def badRequest(text : String) : Result = Result(
    header = ResponseHeader(400, Map.empty),
    body = HttpEntity.Strict(ByteString(text), Some("text/plain"))
  )

  protected def validateJson(implicit jsValue: Option[JsValue]) : JsResult[Any]
}
