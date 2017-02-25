package controllers

import com.google.inject.{Inject, Singleton}
import controllers.actions.AuthAction
import controllers.util.JsonValidationWrapper
import defaults.ApiMethods._
import defaults.TableNames
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

  private implicit val tableName = TableNames.MYPAGE_PROJECTS

  def getProject(projectId : String) = (AuthAction andThen AuthAction.checkPermission(GET_PROJECT)) {
    val optionProject = repo.findOne(ProjectsFieldNames.PROJECT_ID, projectId)
    if(optionProject.isEmpty) NotFound("Project not found")
    else Ok(Json.toJson(optionProject))
  }

  def getAllProjects = (AuthAction andThen AuthAction.checkPermission(GET_ALL_PROJECTS)) {
    val projects = repo.findAll
    Ok(Json.toJson(projects))
  }

  def save = (AuthAction andThen AuthAction.checkPermission(SAVE_PROJECT)) { implicit request =>
    implicit val optionJson = request.body.asJson
    jsonValidator(project => Created(Json.toJson(repo.save(project))))
  }
}
