package controllers

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import controllers.util.{FamilyValidationWrapper, JsonValidationWrapper, UserValidationWrapper}
import repositories.{FamiliesRepo, Repo}

/**
  * @author Romesh Selvan
  */
class ControllersModule extends AbstractModule{
  override def configure(): Unit = {
    bind(classOf[Repo]).annotatedWith(Names.named("Family")).to(classOf[FamiliesRepo])

    bind(classOf[JsonValidationWrapper]).annotatedWith(Names.named("Family")).toInstance(FamilyValidationWrapper)
    bind(classOf[JsonValidationWrapper]).annotatedWith(Names.named("User")).toInstance(UserValidationWrapper)
  }
}
