package dynamoDB

import com.amazonaws.auth.{AWSCredentialsProvider, BasicAWSCredentials}
import defaults.SystemValues

/**
  * @author Romesh Selvan
  */
class AmazonCredProvider extends AWSCredentialsProvider{

  override def getCredentials = new BasicAWSCredentials(SystemValues.AWS_ACCESS_KEY, SystemValues.AWS_SECRET_KEY)

  override def refresh(): Unit = {}
}
