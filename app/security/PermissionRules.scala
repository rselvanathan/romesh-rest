package security

import defaults.ApiMethods._
import defaults.Roles._

/**
  * @author Romesh Selvan
  */
object PermissionRules {
  def apply(methodName : ApiMethod, role : Role) = getRolesForMethod(methodName).contains(role)

  private def getRolesForMethod(methodName : ApiMethod) = methodName match {
    // Family API
    case GET_FAMILY => List(ROMCHARM_APP, ADMIN)
    case SAVE_FAMILY => List(ROMCHARM_APP, ADMIN)
    // User API
    case SAVE_USER => List(ADMIN)
    // Project API
    case GET_PROJECT => List(MYPAGE_APP, ADMIN)
    case GET_ALL_PROJECTS => List(MYPAGE_APP, ADMIN)
    case SAVE_PROJECT => List(ADMIN)
  }
}
