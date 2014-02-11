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

object TaskController extends Controller {
  def findById(id: String) = Action {
    implicit req =>
      Logger.debug("FIND")
      val task: Task = Task("12345", "sender", "recipientA" :: "recipientB" :: Nil, "title")

      render {
        case Accepts.Json() => {
          val links = Links()
          links.addUpdateLink(routes.TaskController.update.toString, "application/json");

          links.add(linkTo(routes.TaskController.findById(task.taskId)).withSelfRel.withJsonMediaType)
          links.add(linkTo(routes.TaskController.delete(task.taskId)).withDeleteRel.withJsonMediaType)

          Ok(links.addAsJsonTo(task)).as(JSON)
        }
        case Accepts.Xml() => {
          val links = Links()

          links.add(linkTo(routes.TaskController.findById(task.taskId)).withSelfRel.withXmlMediaType)

          Ok(links.addAsXmlTo(task)).as(XML)
        }
        case _ => NotAcceptable
      }
  }

  def delete(id: String) = Action {
    implicit req =>
      {
        Logger.debug("DELETE")
        Ok
      }
  }

  def update() = Action(parse.json) { implicit request =>
    Logger.debug("UPDATE")
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
    Logger.debug("CREATE")
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
}