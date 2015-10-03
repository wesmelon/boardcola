package controllers.api

import play.api._
import play.api.mvc._

import models._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import java.sql.Timestamp

import play.api.libs.json._
import play.api.libs.functional.syntax._

class StickyController extends Controller {
  implicit val stickyWrites = new Writes[Sticky] {
      def writes(sticky: Sticky) = Json.obj(
        "id" -> sticky.id,
        "bid" -> sticky.bid,
        "name" -> sticky.name,
        "content" -> sticky.content,
        "x" -> sticky.xPos,
        "y" -> sticky.yPos,
        "creationTime" -> sticky.creationTime,
        "lastModified" -> sticky.lastModified
    )
  }

  implicit val rds0: Reads[Option[String]] = (__ \ "name").read[String].map{ name => Some(name) }
  implicit val wrs0: Writes[Option[String]] = (__ \ "name").write[String].contramap{ (a: Option[String]) => a.get }
  implicit val fmt0: Format[Option[String]] = Format(rds0, wrs0)

  implicit val rds1: Reads[Option[Int]] = (__ \ "pos").read[Int].map{ pos => Some(pos) }
  implicit val wrs1: Writes[Option[Int]] = (__ \ "pos").write[Int].contramap{ (a: Option[Int]) => a.get }
  implicit val fmt1: Format[Option[Int]] = Format(rds1, wrs1)

  implicit val rdst: Reads[Timestamp] = (__ \ "time").read[Long].map{ long => new Timestamp(long) }
  implicit val wrst: Writes[Timestamp] = (__ \ "time").write[Long].contramap{ (a: Timestamp) => a.getTime }
  implicit val fmtt: Format[Timestamp] = Format(rdst, wrst)

  implicit val rdsO: Reads[Option[Timestamp]] = (__ \ "timeopt").read[Long].map{ long => Some(new Timestamp(long)) }
  implicit val wrsO: Writes[Option[Timestamp]] = (__ \ "timeopt").write[Long].contramap{ (a: Option[Timestamp]) => a.get.getTime }
  implicit val fmtO: Format[Option[Timestamp]] = Format(rdsO, wrsO)

  implicit val stickyReads: Reads[Sticky] = (
    (JsPath \ "id").read[Long] and
    (JsPath \ "bid").read[Long] and
    (JsPath \ "name").read[Option[String]] and
    (JsPath \ "content").read[String] and
    (JsPath \ "x").read[Option[Int]] and
    (JsPath \ "y").read[Option[Int]] and
    (JsPath \ "creationTime").read[Timestamp] and
    (JsPath \ "lastModified").read[Option[Timestamp]]
  )(Sticky.apply _)
    
	def getAllStickies = Action.async {
		val f = StickyDAO.findAll()

    f.map(s => Ok(Json.toJson(s)))
	}
}