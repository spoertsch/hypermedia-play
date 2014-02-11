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

object TaskController extends Controller {
  def findTaskById(id: String) = Action {
    implicit req =>
      val task: Task = Task("12345", TaskType("todo", "todo", "ok" :: Nil), "sender", "a" :: "b" :: Nil, "title", "desciption", "application")

      render {
        case Accepts.Json() => {
          val links = Links()
          links.addDeleteLink(routes.TaskController.findTaskById(task.taskId).toString, "application/json");
          links.addUpdateLink(routes.TaskController.findTaskById(task.taskId).toString, "application/json");

          //          links.addSelfLink(routes.TaskController.findTaskById(task.taskId).toString, "application/json");
          links.add(linkTo(routes.TaskController.findTaskById(task.taskId)).withSelfRel.withJsonMediaType)

          Ok(links.addAsJsonTo(task)).as(JSON)
        }
        case Accepts.Xml() => {
          val links = Links()
          links.addDeleteLink(routes.TaskController.findTaskById(task.taskId).toString, "application/xml");
          links.addUpdateLink(routes.TaskController.findTaskById(task.taskId).toString, "application/xml");

          //          links.addSelfLink(routes.TaskController.findTaskById(task.taskId).toString, "application/xml");
          links.add(linkTo(routes.TaskController.findTaskById(task.taskId)).withSelfRel.withXmlMediaType)

          Ok(links.addAsXmlTo(task)).as(XML)
        }
        case _ => NotAcceptable
      }
  }
}