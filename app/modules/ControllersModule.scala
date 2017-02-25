package modules

import com.google.inject.{AbstractModule, Scopes, TypeLiteral}
import controllers.util._
import domain.{Family, Login, Project, User}
import repositories._

/**
  * @author Romesh Selvan
  */
class ControllersModule extends AbstractModule{
  override def configure(): Unit = {
    bind(new TypeLiteral[Repo[Family]] {}).to(classOf[FamiliesRepo]).in(Scopes.SINGLETON)
    bind(new TypeLiteral[Repo[User]] {}).to(classOf[UsersRepo]).in(Scopes.SINGLETON)
    bind(new TypeLiteral[Repo[Project]] {}).to(classOf[ProjectRepo]).in(Scopes.SINGLETON)

    bind(new TypeLiteral[JsonValidationWrapper[Family]]{}).toInstance(FamilyValidationWrapper)
    bind(new TypeLiteral[JsonValidationWrapper[Login]]{}).toInstance(LoginValidationWrapper)
    bind(new TypeLiteral[JsonValidationWrapper[User]]{}).toInstance(UserValidationWrapper)
    bind(new TypeLiteral[JsonValidationWrapper[Project]]{}).toInstance(ProjectValidationWrapper)
  }
}
