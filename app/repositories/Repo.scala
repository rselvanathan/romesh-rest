package repositories

import com.amazonaws.services.dynamodbv2.document.Table

/**
  * @author Romesh Selvan
  */
trait Repo {
  type T

  def findOne(id : String) : T

  def findAll() : Seq[T]

  def save(_object : T) : T

  protected def getTable : Table
}
