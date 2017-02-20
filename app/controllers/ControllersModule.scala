package controllers

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import repositories.{FamiliesRepo, Repo}

/**
  * @author Romesh Selvan
  */
class ControllersModule extends AbstractModule{
  override def configure(): Unit = {
    bind(classOf[Repo]).annotatedWith(Names.named("Family")).to(classOf[FamiliesRepo])
  }
}
