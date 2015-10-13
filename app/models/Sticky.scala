package models

import java.sql.Timestamp

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Sticky(
  id: Option[Long], 
  bid: Long, 
  name: Option[String], 
  content: String, 
  xPos: Option[Int], 
  yPos: Option[Int], 
  creationTime: Option[Timestamp], 
  lastModified: Option[Timestamp])

object Sticky {
  implicit val TimestampReader: Reads[Timestamp] = (__ \ "time").read[Long].map{ long => new Timestamp(long) }
  implicit val TimestampWriter: Writes[Timestamp] = (__ \ "time").write[Long].contramap{ (a: Timestamp) => a.getTime }
  implicit val TimestampFormat: Format[Timestamp] = Format(TimestampReader, TimestampWriter)

  implicit val WriteSticky = new Writes[Sticky] {
      def writes(sticky: Sticky) = Json.obj(
        "id" -> sticky.id,
        "bid" -> sticky.bid,
        "name" -> sticky.name,
        "content" -> sticky.content,
        "x" -> sticky.xPos,
        "y" -> sticky.yPos,
        "creation_time" -> sticky.creationTime,
        "last_modified" -> sticky.lastModified
    )
  }

  implicit val ReadSticky: Reads[Sticky] = (
    (JsPath \ "id").readNullable[Long] and
    (JsPath \ "bid").read[Long] and
    (JsPath \ "name").readNullable[String] and
    (JsPath \ "content").read[String] and
    (JsPath \ "x").readNullable[Int] and
    (JsPath \ "y").readNullable[Int] and
    (JsPath \ "creation_time").readNullable[Timestamp] and
    (JsPath \ "last_modified").readNullable[Timestamp]
  )(Sticky.apply _)
}