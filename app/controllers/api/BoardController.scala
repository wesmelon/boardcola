package controllers.api

import play.api._
import play.api.mvc._

import models._
import play.api.libs.json._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import java.sql.Timestamp

import play.api.libs.json._
import play.api.libs.functional.syntax._

class BoardController extends Controller {
  implicit val boardWrites = new Writes[Board] {
      def writes(board: Board) = Json.obj(
        "id" -> board.id,
        "cid" -> board.cid,
        "name" -> board.name,
        "creationTime" -> board.creationTime,
        "lastModified" -> board.lastModified
    )
  }

  implicit val rds: Reads[Timestamp] = (__ \ "time").read[Long].map{ long => new Timestamp(long) }
  implicit val wrs: Writes[Timestamp] = (__ \ "time").write[Long].contramap{ (a: Timestamp) => a.getTime }
  implicit val fmt: Format[Timestamp] = Format(rds, wrs)

  implicit val rdsO: Reads[Option[Timestamp]] = (__ \ "timeopt").read[Long].map{ long => Some(new Timestamp(long)) }
  implicit val wrsO: Writes[Option[Timestamp]] = (__ \ "timeopt").write[Long].contramap{ (a: Option[Timestamp]) => a.get.getTime }
  implicit val fmtO: Format[Option[Timestamp]] = Format(rdsO, wrsO)

  implicit val boardReads: Reads[Board] = (
    (JsPath \ "id").read[Long] and
    (JsPath \ "cid").read[Long] and
    (JsPath \ "name").read[String] and
    (JsPath \ "name").read[Timestamp] and
    (JsPath \ "name").read[Option[Timestamp]]
  )(Board.apply _)

  def getAllBoards = Action.async {
    val f = BoardDAO.findAll()
    f.map(s => Ok(Json.toJson(s)))
  }
}