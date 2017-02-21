package controllers

import com.google.inject.{AbstractModule, Scopes, TypeLiteral}
import com.google.inject.name.Names
import controllers.util.{FamilyValidationWrapper, JsonValidationWrapper, UserValidationWrapper}
import domain.{Family, User}
import repositories._

/**
  * @author Romesh Selvan
  */
class ControllersModule extends AbstractModule{
  override def configure(): Unit = {
    bind(new TypeLiteral[Repo[Family]] {}).to(classOf[FamiliesRepo]).in(Scopes.SINGLETON)
    bind(new TypeLiteral[Repo[User]] {}).to(classOf[UsersRepo]).in(Scopes.SINGLETON)

    bind(classOf[JsonValidationWrapper]).annotatedWith(Names.named("Family")).toInstance(FamilyValidationWrapper)
    bind(classOf[JsonValidationWrapper]).annotatedWith(Names.named("User")).toInstance(UserValidationWrapper)
  }
}
