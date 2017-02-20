package controllers.util

import akka.util.ByteString
import play.api.http.HttpEntity
import play.api.libs.json.JsValue
import play.api.mvc.{ResponseHeader, Result}

/**
  * @author Romesh Selvan
  */
trait JsonValidationWrapper {

  /**
    * Will take a JsValue and a function as a parameter. The function should process the JSON that will be passed into it
    * and return back a `Result`.
    * @param jsValue - the jsValue to validate
    * @param successFunction - the function to apply to the validated jsValue
    * @tparam T - Domain type of the json value
    * @return Result - A result to return back to the REST user
    */
  def apply[T](implicit jsValue : Option[JsValue], successFunction:T => Result) : Result = {
    if(jsValue.get == null) badRequest("No Json body was found")
    else {
      validate[T](successFunction)
    }
  }

  protected def badRequest(text : String) : Result = Result(
    header = ResponseHeader(400, Map.empty),
    body = HttpEntity.Strict(ByteString(text), Some("text/plain"))
  )

  /**
    * The domain specific validation, which will be completed before executing the processing function that is passed in as a parameter.
    * If the validation fails then a 400 Bad Request Result is returned. Otherwise the processing function will be executed.
    * @param successFunction - the function to apply to the validated jsValue
    * @param jsValue - The json value to apply the domain specific validation
    * @tparam T - Domain Type
    * @return - Return the Result
    */
  protected def validate[T](successFunction:T => Result)(implicit jsValue: Option[JsValue]) : Result
}
