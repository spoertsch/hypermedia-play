package model

import play.api.libs.json.Json

case class TaskType(
  name: String,
  code: String,
  outcomes: List[String]) {

  def toXml = {
    <taskType>
      <name>{ name }</name>
      <code>{ code }</code>
      { for (outcome <- outcomes) yield <outcome>{ outcome }</outcome> }
    </taskType>
  }
}

object TaskType {
  implicit val taskTypeWrites = Json.writes[TaskType]
  implicit val taskTypeReads = Json.reads[TaskType]
}