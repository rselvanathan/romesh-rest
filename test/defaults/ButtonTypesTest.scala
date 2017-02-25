package defaults

import org.scalatest.{FunSuite, Matchers}
import ButtonTypes._

/**
  * @author Romesh Selvan
  */
class ButtonTypesTest extends FunSuite with Matchers {
  
  test("When Button Type is GITHUB return GITHUB string") {
    ButtonTypes.buttonTypeToString(GITHUB) should be ("GITHUB")
  }

  test("When Button Type is DIRECT_LINK return DIRECT_LINK string") {
    ButtonTypes.buttonTypeToString(DIRECT_LINK) should be ("DIRECT_LINK")
  }

  test("When Button Type is VIDEO return VIDEO string") {
    ButtonTypes.buttonTypeToString(VIDEO) should be ("VIDEO")
  }
  
  test("When Button Type is GALLERY return GALLERY string") {
    ButtonTypes.buttonTypeToString(GALLERY) should be ("GALLERY")
  }

  test("When String is GITHUB return GITHUB Button Type") {
    ButtonTypes.stringToButtonType("GITHUB") should be (GITHUB)
  }

  test("When String is VIDEO return VIDEO Button Type") {
    ButtonTypes.stringToButtonType("VIDEO") should be (VIDEO)
  }

  test("When String is GALLERY return GALLERY Button Type") {
    ButtonTypes.stringToButtonType("GALLERY") should be (GALLERY)
  }

  test("When String is DIRECT_LINK return DIRECT_LINK Button Type") {
    ButtonTypes.stringToButtonType("DIRECT_LINK") should be (DIRECT_LINK)
  }
}
