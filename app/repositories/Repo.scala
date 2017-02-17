package repositories

/**
  * @author Romesh Selvan
  */
trait Repo {
  type T

  def findOne(id : String) : T

  def findAll() : Seq[T]

  def save(_object : T) : T
}
