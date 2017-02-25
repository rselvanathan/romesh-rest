package defaults

/**
  * @author Romesh Selvan
  */
object Roles {

  trait Role

  object ROMCHARM_APP extends Role
  object MYPAGE_APP extends Role
  object ADMIN extends Role

  def getRole(value : String) = value match {
    case "ROLE_ROMCHARM_APP" => ROMCHARM_APP
    case "ROLE_MYPAGE_APP" => MYPAGE_APP
    case "ROLE_ADMIN" => ADMIN
  }

  def getRoleString(role : Role) = role match {
    case ROMCHARM_APP => "ROLE_ROMCHARM_APP"
    case MYPAGE_APP => "ROLE_MYPAGE_APP"
    case ADMIN => "ROLE_ADMIN"
  }
}
