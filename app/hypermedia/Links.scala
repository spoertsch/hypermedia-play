package hypermedia

import play.api.libs.json.JsObject
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json.Writes
import scala.xml.Elem

case class Links() {

  var _links: List[Link] = List()

  def addSelfLink(uri: String, mediaType: String) {
    _links = Link(SELF, uri, mediaType) :: _links
  }

  def addGetLink(uri: String, mediaType: String) {
    _links = Link(GET, uri, mediaType) :: _links
  }

  def addCreateLink(uri: String, mediaType: String) {
    _links = Link(CREATE, uri, mediaType) :: _links
  }

  def addUpdateLink(uri: String, mediaType: String) {
    _links = Link(UPDATE, uri, mediaType) :: _links
  }

  def addDeleteLink(uri: String, mediaType: String) {
    _links = Link(DELETE, uri, mediaType) :: _links
  }

  def add(link: Link) {
    _links = link :: _links
  }

  def addAsJsonTo(jsValue: JsValue): JsObject = {
    jsValue.as[JsObject] ++ asJson
  }

  def asJson: JsObject = {
    Json.obj(
      "_links" -> Json.toJson(_links))
  }

  def addAsJsonTo[T](obj: T)(implicit anyWrites: Writes[T]): JsObject = {
    Json.toJson(obj).as[JsObject] ++ asJson
  }

  def asXml: Elem = {
    <_links>
      { _links.map { l => <link rel={ l.rel.name }><uri mediaType={ l.mediaType }>{ l.uri }</uri></link> } }
    </_links>
  }

  // TODO: Need to find a better way of adding links as xml instead of wrapping response in <result>
  def addAsXmlTo[T](obj: T)(implicit objToXml: T => Elem): Elem = {
    <result>{ objToXml(obj) ++ asXml }</result>
  }
}

