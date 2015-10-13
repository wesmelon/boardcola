package models

import java.util.UUID
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Category(id: Option[Long], uid: Option[UUID], name: String)

object Category {  
  implicit val categoryWrites = new Writes[Category] {
      def writes(category: Category) = Json.obj(
        "id" -> category.id,
        "uid" -> category.uid,
        "name" -> category.name
    )
  }

  implicit val categoryReads: Reads[Category] = (
    (JsPath \ "id").readNullable[Long] and
    (JsPath \ "uid").readNullable[UUID] and
    (JsPath \ "name").read[String]
  )(Category.apply _)
}