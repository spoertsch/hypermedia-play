package hypermedia

sealed trait Rel { def name: String }
case object NONE extends Rel { val name = "" }
case object SELF extends Rel { val name = "self" }
case object UPDATE extends Rel { val name = "update" }
case object GET extends Rel { val name = "get" }
case object DELETE extends Rel { val name = "delete" }
case object CREATE extends Rel { val name = "create" }
case class OTHER(val rel: String) extends Rel { val name = rel }