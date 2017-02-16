package dynamoDB.tableFields

/**
  * @author Romesh Selvan
  */
object FamiliesFieldNames {

  sealed abstract class FamilyFieldNames(value : String) {
    override def toString = value
  }

  case object EMAIL extends FamilyFieldNames("email")
  case object FIRST_NAME extends FamilyFieldNames("firstName")
  case object LAST_NAME extends FamilyFieldNames("lastName")
  case object ARE_ATTENDING extends FamilyFieldNames("areAttending")
  case object NUMBER_ATTENDING extends FamilyFieldNames("numberAttending")
}
