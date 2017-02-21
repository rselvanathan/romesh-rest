package controllers

import com.google.inject.{AbstractModule, Scopes, TypeLiteral}
import controllers.util.{FamilyValidationWrapper, JsonValidationWrapper, LoginValidationWrapper, UserValidationWrapper}
import domain.{Family, Login, User}
import repositories._

/**
  * @author Romesh Selvan
  */
class ControllersModule extends AbstractModule{
  override def configure(): Unit = {
    bind(new TypeLiteral[Repo[Family]] {}).to(classOf[FamiliesRepo]).in(Scopes.SINGLETON)
    bind(new TypeLiteral[Repo[User]] {}).to(classOf[UsersRepo]).in(Scopes.SINGLETON)

    bind(new TypeLiteral[JsonValidationWrapper[Family]]{}).toInstance(FamilyValidationWrapper)
    bind(new TypeLiteral[JsonValidationWrapper[Login]]{}).toInstance(LoginValidationWrapper)
    bind(new TypeLiteral[JsonValidationWrapper[User]]{}).toInstance(UserValidationWrapper)
  }
}
