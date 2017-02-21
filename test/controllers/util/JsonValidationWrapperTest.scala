package controllers.util

import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}
import play.api.libs.json.{JsResult, JsValue, Json}
import play.api.mvc.Result

/**
  * @author Romesh Selvan
  */
case class TestDomain(fieldString : String, fieldInt : Int)

object TestDomain {
  implicit val reader = Json.reads[TestDomain]
  implicit val writer = Json.writes[TestDomain]
}

object TestWrapper extends JsonValidationWrapper[TestDomain] {
  override protected def validate(implicit jsValue: Option[JsValue]): JsResult[TestDomain] = jsValue.get.validate[TestDomain]
}

class JsonValidationWrapperTest extends FunSuite with BeforeAndAfter with Matchers {

  var SUCCESS_CALLED = false

  val valWrapper : JsonValidationWrapper[TestDomain] = TestWrapper

  before {
    SUCCESS_CALLED = false
  }

  test("When Json passed in is null expect a 400 bad request") {
    implicit val json : Option[JsValue] = Some(null)
    val result = valWrapper.apply(success => defaultSuccessFunction(success))
    val statusCode = result.header.status

    statusCode should be (400)
    SUCCESS_CALLED should be (false)
  }

  test("When Json passed in fails validation expect a 400 bad request") {
    implicit val json : Option[JsValue] = Some(Json.parse(s"""{"fieldString":"Value","fieldInt":"wrong"}"""))
    val result = valWrapper.apply(success => defaultSuccessFunction(success))
    val statusCode = result.header.status

    statusCode should be (400)
    SUCCESS_CALLED should be (false)
  }

  test("When Json passed in passes validation expect a 400 bad request") {
    implicit val json : Option[JsValue] = Some(Json.parse(s"""{"fieldString":"Value","fieldInt":22}"""))
    val result = valWrapper.apply(success => defaultSuccessFunction(success))

    SUCCESS_CALLED should be (true)
  }

  def defaultSuccessFunction(testDomain: TestDomain) : Result = {
    SUCCESS_CALLED = true
    null
  }
}
