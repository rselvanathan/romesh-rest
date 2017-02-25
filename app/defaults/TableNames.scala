package defaults

/**
  * @author Romesh Selvan
  */
object TableNames {

  trait TableName
  object ROMCHARM_FAMILY extends TableName
  object USER_ROLES      extends TableName
  object MYPAGE_PROJECTS extends TableName

  def getTableName(tableName: TableName) = tableName match {
    case ROMCHARM_FAMILY => "romcharm-families"
    case USER_ROLES      => "romcharm-userRoles"
    case MYPAGE_PROJECTS => "mypage-projects"
  }
}
