package hypermedia

import play.api.libs.json.Writes
import play.api.libs.json.JsValue
import play.api.mvc.Call
import play.api.libs.json.Json

case class Link(rel: Rel, uri: String, mediaType: String) {

  def withRel(rel: String): Link = {
    withRel(OTHER(rel))
  }

  def withSelfRel: Link = {
    withRel(SELF)
  }

  def withGetRel: Link = {
    withRel(GET)
  }

  def withUpdateRel: Link = {
    withRel(UPDATE)
  }

  def withCreateRel: Link = {
    withRel(CREATE)
  }

  def withDeleteRel: Link = {
    withRel(DELETE)
  }

  def withRel(rel: Rel): Link = {
    this.copy(rel = rel)
  }

  def withJsonMediaType: Link = {
    withMediaType("application/json")
  }
  
  def withXmlMediaType: Link = {
    withMediaType("application/xml")
  }

  def withMediaType(mediaType: String): Link = {
    this.copy(mediaType = mediaType)
  }
}

object Link {

  implicit val linkWrites = new Writes[Link] {
    def writes(l: Link): JsValue = {
      Json.obj(
        "rel" -> l.rel.name,
        "uri" -> l.uri,
        "mediaType" -> l.mediaType)
    }
  }

  def linkTo(call: Call): Link = {
    Link(NONE, call.toString, "")
  }

  def linkTo(uri: String): Link = {
    Link(NONE, uri, "")
  }
}