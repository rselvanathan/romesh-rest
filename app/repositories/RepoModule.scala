package repositories

import com.google.inject.{AbstractModule, TypeLiteral}
import domain.{Family, Project, User}
import dynamoDB.converters.{DynamoDBConverter, FamilyDynamoConverter, ProjectDynamoConverter, UserDynamoConverter}

/**
  * @author Romesh Selvan
  */
class RepoModule extends AbstractModule{
  override def configure(): Unit = {
    bind(new TypeLiteral[DynamoDBConverter[Family]]{}).toInstance(FamilyDynamoConverter)
    bind(new TypeLiteral[DynamoDBConverter[User]]{}).toInstance(UserDynamoConverter)
    bind(new TypeLiteral[DynamoDBConverter[Project]]{}).toInstance(ProjectDynamoConverter)
  }
}
