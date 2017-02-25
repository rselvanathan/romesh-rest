package repositories

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.google.inject.Inject
import domain.Project
import dynamoDB.converters.DynamoDBConverter

/**
  * @author Romesh Selvan
  */
class ProjectRepo @Inject() (dynamoDB : DynamoDB, dynamoDBConverter: DynamoDBConverter[Project])
  extends RepoImpl[Project](dynamoDB, dynamoDBConverter) {

  override def findAll(implicit tableName: String): Seq[Project] = super.findAll.sortWith(_.order.get < _.order.get)

  override def save(_object: Project)(implicit tableName: String): Project = null
}
