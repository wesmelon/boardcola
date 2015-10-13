package models

import java.util.UUID

case class Category(id: Option[Long], uid: Option[UUID], name: String)
