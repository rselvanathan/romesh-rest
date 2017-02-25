package modules

import com.amazonaws.regions.Regions
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.google.inject.{AbstractModule, Inject, Provides, Singleton}
import dynamoDB.AmazonCredProvider

/**
  * @author Romesh Selvan
  */
class DynamoDBModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[AmazonCredProvider])
  }

  @Provides() @Singleton()
  @Inject()
  def dynamoDB(awsCredentials : AmazonCredProvider) : DynamoDB = {
    new DynamoDB(AmazonDynamoDBClientBuilder.standard()
      .withCredentials(awsCredentials)
      .withRegion(Regions.EU_WEST_1)
      .build())
  }
}
