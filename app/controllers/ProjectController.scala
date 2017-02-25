package controllers

import com.google.inject.{Inject, Singleton}
import controllers.actions.AuthAction
import controllers.util.JsonValidationWrapper
import defaults.ApiMethods._
import domain.Project
import dynamoDB.tableFields.ProjectsFieldNames
import play.api.libs.json.Json
import play.api.mvc.Controller
import repositories.Repo

/**
  * @author Romesh Selvan
  */
@Singleton
class ProjectController @Inject() (repo : Repo[Project],
                                   jsonValidator : JsonValidationWrapper[Project]) extends Controller {

  private implicit val tableName = "mypage-projects"

  def getProject(projectId : String) = (AuthAction andThen AuthAction.checkPermission(GET_PROJECT)) {
    val optionProject = repo.findOne(ProjectsFieldNames.PROJECT_ID, projectId)
    if(optionProject.isEmpty) NotFound("Project not found")
    else Ok(Json.toJson(optionProject))
  }

  def getAllProjects = (AuthAction andThen AuthAction.checkPermission(GET_ALL_PROJECTS)) {
    val projects = repo.findAll
    Ok(Json.toJson(projects))
  }
}
