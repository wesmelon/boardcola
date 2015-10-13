package models

import java.util.UUID
import java.sql.Timestamp

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Board(
  id: Option[Long], 
  uid: Option[UUID], 
  cid: Option[Long], 
  name: String, 
  creationTime: Option[Timestamp], 
  lastModified: Option[Timestamp])

object Board {
  implicit val TimestampReader: Reads[Timestamp] = (__ \ "time").read[Long].map{ long => new Timestamp(long) }
  implicit val TimestampWriter: Writes[Timestamp] = (__ \ "time").write[Long].contramap{ (a: Timestamp) => a.getTime }
  implicit val TimestampFormat: Format[Timestamp] = Format(TimestampReader, TimestampWriter)

  implicit val WriteBoard = new Writes[Board] {
      def writes(board: Board) = Json.obj(
        "id" -> board.id,
        "uid" -> board.uid,
        "cid" -> board.cid,
        "name" -> board.name,
        "creation_time" -> board.creationTime,
        "last_modified" -> board.lastModified
    )
  }

  implicit val ReadBoard: Reads[Board] = (
    (JsPath \ "id").readNullable[Long] and
    (JsPath \ "uid").readNullable[UUID] and
    (JsPath \ "cid").readNullable[Long] and
    (JsPath \ "name").read[String] and
    (JsPath \ "creation_time").readNullable[Timestamp] and
    (JsPath \ "last_modified").readNullable[Timestamp]
  )(Board.apply _)
}