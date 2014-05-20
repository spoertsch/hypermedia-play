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
    add(Link(SELF, uri, mediaType))
  }

  def addGetLink(uri: String, mediaType: String) {
    add(Link(GET, uri, mediaType))
  }

  def addCreateLink(uri: String, mediaType: String) {
    add(Link(CREATE, uri, mediaType))
  }

  def addUpdateLink(uri: String, mediaType: String) {
    add(Link(UPDATE, uri, mediaType))
  }

  def addDeleteLink(uri: String, mediaType: String) {
    add(Link(DELETE, uri, mediaType))
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

  def addAsJsonTo[T](obj: T)(implicit objWrites: Writes[T]): JsObject = {
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

  def addAsXmlTo[T](list: Seq[T])(implicit objToXml: T => Elem): Elem = {
    <result>{
      list.map(objToXml(_) ++ asXml)
    }</result>
  }

}

object Links {

  def generateAsJson[T](obj: T, linkGenerator: T => Links)(implicit objWrites: Writes[T]): JsObject = {
    Json.toJson(obj).as[JsObject] ++ linkGenerator(obj).asJson
  }

  def generateAsJson[T](list: Seq[T], linkGenerator: T => Links)(implicit objWrites: Writes[T]): JsObject = {
    JsObject(Seq("collection" -> Json.toJson(list.map(generateAsJson(_, linkGenerator)))))
  }
  
  def generateAsJsonImplicit[T](obj: T)(implicit linkGenerator: T => Links, objWrites: Writes[T]): JsObject = {
    Json.toJson(obj).as[JsObject] ++ linkGenerator(obj).asJson
  }

  def generateAsJsonImplicit[T](list: Seq[T])(implicit linkGenerator: T => Links, objWrites: Writes[T]): JsObject = {
    JsObject(Seq("collection" -> Json.toJson(list.map(generateAsJson(_, linkGenerator)))))
  }
}

