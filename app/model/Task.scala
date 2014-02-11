package model

import java.sql.Date
import play.api.libs.json.Reads
import play.api.libs.json.Writes
import play.api.libs.json.Json
import scala.xml.Elem

case class Task(
  taskId: String,
  //  taskType: TaskType,
  sender: String,
  recipients: List[String],
  title: String //,
  //  description: String,
  //  application: String,
  //    createdOn: Date,
  //    dueDate: Date,
  //    expiryDate: Date,
  //    outcome: String,
  ) {

}

object Task {
  implicit val taskWrites = Json.writes[Task]
  implicit val taskReads = Json.reads[Task]

  implicit def toXml(task: Task): Elem = {
    <task>
      <taskId>{ task.taskId }</taskId>
      <sender>{ task.sender }</sender>
      { for (recipient <- task.recipients) yield <recipient>{ recipient }</recipient> }
      <title>{ task.title }</title>
    </task>
  }
}

