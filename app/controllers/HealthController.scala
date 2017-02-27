package controllers

import com.google.inject.Singleton
import play.api.mvc.{Action, Controller}

/**
  * @author Romesh Selvan
  */
@Singleton
class HealthController extends Controller {

  def health = Action {
    Ok("Healthy")
  }
}
