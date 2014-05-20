package controllers

import play.api.mvc.Controller
import play.api.mvc.Action
import model.Task
import model.TaskType
import play.api.libs.json.Json
import model._
import hypermedia.Link._
import play.api.libs.json.JsObject
import hypermedia.Links
import play.api.libs.json.JsError
import play.api.Logger
import hypermedia.Links
import hypermedia.LinkBuilder

object TaskController extends Controller {

  def findById(id: String) = Action {
    implicit req =>
      val task = Task.findById(id)

      render {
        case Accepts.Json() => {
          val links = Links()
          links.addUpdateLink(routes.TaskController.update.toString, "application/json");

          links.add(linkTo(routes.TaskController.findById(task.taskId)).withSelfRel.withJsonMediaType)
          links.add(linkTo(routes.TaskController.delete(task.taskId)).withDeleteRel.withJsonMediaType)

          Ok(links.addAsJsonTo(task)).as(JSON)
          //          Ok(Links.generateAsJson(task, createDefaultTaskLinksAsJson _)).as(JSON)
        }
        case Accepts.Xml() => {
          val links = Links()

          links.add(linkTo(routes.TaskController.findById(task.taskId)).withSelfRel.withXmlMediaType)

          Ok(links.addAsXmlTo(task)).as(XML)
        }
        case _ => NotAcceptable
      }
  }

  private def generateDefaultTaskLinksAsJson(task: Task): Links = {
    val links = Links()
    links.addUpdateLink(routes.TaskController.update.toString, "application/json");

    links.add(linkTo(routes.TaskController.findById(task.taskId)).withSelfRel.withJsonMediaType)
    links.add(linkTo(routes.TaskController.delete(task.taskId)).withDeleteRel.withJsonMediaType)

    links
  }

  implicit def generateDefaultTaskLinksAsJsonImplicit(task: Task): Links = {
    val links = Links()
    links.addUpdateLink(routes.TaskController.update.toString, "application/json");

    links.add(linkTo(routes.TaskController.findById(task.taskId)).withSelfRel.withJsonMediaType)
    links.add(linkTo(routes.TaskController.delete(task.taskId)).withDeleteRel.withJsonMediaType)

    links
  }

  def findAll() = Action {
    implicit req =>
      val taskList = Task.findAll

      render {
        case Accepts.Json() => {
          Ok(Links.generateAsJsonImplicit(taskList)).as(JSON)
        }
        case _ => NotAcceptable
      }
  }

  def delete(id: String) = Action {
    implicit req =>
      {
        NoContent
      }
  }

  def update() = Action(parse.json) { implicit request =>
    request.body.validate[Task].map {
      case task => {
        val links = Links()

        links.add(linkTo(routes.TaskController.findById(task.taskId)).withSelfRel.withJsonMediaType)
        links.add(linkTo(routes.TaskController.update).withUpdateRel.withJsonMediaType)
        links.add(linkTo(routes.TaskController.delete(task.taskId)).withDeleteRel.withJsonMediaType)

        Ok(links.addAsJsonTo(task)).as(JSON)
      }
    }.recoverTotal {
      e => BadRequest("Detected error:" + JsError.toFlatJson(e))
    }
  }

  def create() = Action(parse.json) { implicit request =>
    request.body.validate[Task].map {
      case task => {
        val links = Links()

        links.add(linkTo(routes.TaskController.findById(task.taskId)).withSelfRel.withJsonMediaType)
        links.add(linkTo(routes.TaskController.update).withUpdateRel.withJsonMediaType)
        links.add(linkTo(routes.TaskController.delete(task.taskId)).withDeleteRel.withJsonMediaType)

        Ok(links.addAsJsonTo(task)).as(JSON)
        //        Created.withHeaders(
        //          "Location" -> routes.TaskController.findById(task.id).absoluteURL())
      }
    }.recoverTotal {
      e => BadRequest("Detected error:" + JsError.toFlatJson(e))
    }
  }

}