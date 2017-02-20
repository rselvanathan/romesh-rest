package repositories

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import dynamoDB.converters.{DynamoDBConverter, FamilyDynamoConverter, UserDynamoConverter}

/**
  * @author Romesh Selvan
  */
class RepoModule extends AbstractModule{
  override def configure(): Unit = {
    bind(classOf[DynamoDBConverter]).annotatedWith(Names.named("Family")).toInstance(FamilyDynamoConverter)
    bind(classOf[DynamoDBConverter]).annotatedWith(Names.named("User")).toInstance(UserDynamoConverter)
  }
}
