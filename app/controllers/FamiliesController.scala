package controllers

import com.google.inject.Singleton
import play.api.mvc.{Action, Controller}

/**
  * @author Romesh Selvan
  */
@Singleton
class FamiliesController extends Controller {

  def index = Action {
    Ok("Hello World!")
  }
}
