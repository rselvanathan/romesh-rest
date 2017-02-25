package repositories

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.google.inject.Inject
import defaults.TableNames.TableName
import domain.Project
import dynamoDB.converters.DynamoDBConverter

/**
  * @author Romesh Selvan
  */
class ProjectRepo @Inject() (dynamoDB : DynamoDB, dynamoDBConverter: DynamoDBConverter[Project])
  extends RepoImpl[Project](dynamoDB, dynamoDBConverter) {

  override def findAll(implicit tableName: TableName): Seq[Project] = super.findAll.sortWith(_.order.get < _.order.get)

  /**
    * Save a project.
    *
    * The logic is as follows :
    *
    * - If the current project list is empty then add the project as the first in order
    * - If the project already exists then do the following :
    *      - If the project order has not been set, save it at the same position
    *      - If the project is being moved further down, then shift projects between its current position and new position,
    *        to the left. If the order number is greater than the number of projects then just place it at the back of the list,
    *        while shifting all the project, from the existing position to the last position, one position to the left
    *      - If the project is being moved up in the list, then shift projects between its current position and new position,
    *        to the right
    *      - If none of the above conditions are met then just do nothing and save it in the same position
    *- If the project is new do the following :
    *      - If the project order has not been set or it is beyond the current project list size then add it to the back
    *      - If it is to replace another project in the order, then simply shift all the projects from the position specified
    *        to the right
    *
    * @param projectToSave The project to be saved
    * @param tableName - (Implicit) The Table to save the object too
    * @return The project that has been saved
    */
  override def save(projectToSave: Project)(implicit tableName: TableName): Project = {
    val currentOrderedProjects = findAll
    val alreadyExistingProject = currentOrderedProjects.find(_.projectId == projectToSave.projectId)

    var finalProjectToSave = projectToSave

    // Save the project as the first if the list is empty
    if(currentOrderedProjects.isEmpty) {
      finalProjectToSave = getNewOrderProject(projectToSave, 1)
    }
    // If the project already exists
    else if(alreadyExistingProject.isDefined) {
      finalProjectToSave = processExistingProject(projectToSave, alreadyExistingProject.get, currentOrderedProjects)
    }
    // If the project is new and has no order set or the order set is higher than the project list total, just add it to the end.
    else if(doesProjectHaveValidOrderNumber(projectToSave, currentOrderedProjects.size)) {
        val lastOrder = currentOrderedProjects.last.order.get
        finalProjectToSave = getNewOrderProject(projectToSave, lastOrder + 1)
    }
    // Otherwise if the project is new, move all the projects between the position defined and the end of the list, one position to the right -
    // increasing the size of the list by one.
    else {
      saveProjectsWithNewOrder(currentOrderedProjects, projectToSave.order.get - 1, currentOrderedProjects.size, increment = true)
    }
    super.save(finalProjectToSave)
    projectToSave
  }

  private def processExistingProject(projectToSave : Project, exsitingProject : Project,currentOrderedProjects : Seq[Project])(implicit tableName: TableName) : Project = {
    // Project has not order defined, then set it to the same as before
    if (projectToSave.order.get <= 0) {
      return getNewOrderProject(projectToSave, exsitingProject.order.get)
    }
    // Project moved further down the list - then move objects that are between, its old and new position including the
    // object to be replaced, one position to the left. If the project has a order defined which is higher than the total
    // list size then just place it at the end of the list instead, while shifting the rest one position to the left.
    else if (exsitingProject.order.get < projectToSave.order.get) {
      if (projectToSave.order.get > currentOrderedProjects.size) {
        saveProjectsWithNewOrder(currentOrderedProjects, exsitingProject.order.get, currentOrderedProjects.size, increment = false)
        return getNewOrderProject(projectToSave, currentOrderedProjects.size)
      }
      else {
        saveProjectsWithNewOrder(currentOrderedProjects, exsitingProject.order.get, projectToSave.order.get, increment = false)
      }
    }
    // If the project moves up towards the beginning of the list, then move projects that are between its old and new position, including
    // the project to be replaced one position to the right.
    else if (exsitingProject.order.get > projectToSave.order.get) {
      saveProjectsWithNewOrder(currentOrderedProjects, projectToSave.order.get - 1, exsitingProject.order.get, increment = true)
    }
    projectToSave
  }

  private def doesProjectHaveValidOrderNumber(project: Project, currentProjectListSize : Int) = project.order.get <= 0 || currentProjectListSize < project.order.get

  private def saveProjectsWithNewOrder(projectList : Seq[Project], subListInclusiveStart : Int, subListExclusiveEnd : Int, increment : Boolean)(implicit tableName: TableName) = {
    projectList.slice(subListInclusiveStart, subListExclusiveEnd).foreach({ projectToSave =>
      super.save(getNewProjectWithIncrementOrDecrement(projectToSave, increment))
    })
  }

  private def getNewProjectWithIncrementOrDecrement(project: Project, increment : Boolean) = {
    if (increment) getNewOrderProject(project, project.order.get + 1)
    else getNewOrderProject(project, project.order.get - 1)
  }

  private def getNewOrderProject(project: Project, order : Int) =
    Project(
      project.projectId,
      project.projectTitle,
      project.titleImageLink,
      project.buttonTypes,
      project.githubLink,
      project.videoLink,
      project.galleryLinks,
      project.directLink,
      Option(order)
    )
}
