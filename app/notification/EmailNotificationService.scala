package notification

import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sns.model.{MessageAttributeValue, PublishRequest}
import com.google.inject.Inject
import defaults.SystemValues
import domain.Family
import play.api.libs.json.Json
import collection.JavaConverters._

/**
  * @author Romesh Selvan
  */
class EmailNotificationService @Inject()(amazonSNSClient: AmazonSNSClient) {

  def sendEmailNotification(family: Family) = {
    val request = new PublishRequest(SystemValues.AWS_SNS_TOPIC, Json.toJson(family).toString())
    val attri = Map("apptype" -> new MessageAttributeValue().withDataType("String").withStringValue(SystemValues.APP_TYPE))
    request.setMessageAttributes(attri.asJava)
    amazonSNSClient.publish(request)
  }
}
