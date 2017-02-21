package repositories

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.google.inject.Inject
import domain.Family
import dynamoDB.converters.DynamoDBConverter

/**
  * @author Romesh Selvan
  */
class FamiliesRepo @Inject() (dynamoDB : DynamoDB, dynamoDBConverter: DynamoDBConverter[Family])
  extends RepoImpl[Family](dynamoDB, dynamoDBConverter)
