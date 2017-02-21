package repositories

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.google.inject.Inject
import domain.User
import dynamoDB.converters.DynamoDBConverter

/**
  * @author Romesh Selvan
  */
class UsersRepo @Inject() (dynamoDB : DynamoDB, dynamoDBConverter: DynamoDBConverter[User])
  extends RepoImpl[User](dynamoDB, dynamoDBConverter)
