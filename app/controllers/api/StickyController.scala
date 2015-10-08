package controllers.api

import javax.inject.Inject
import play.api.i18n.MessagesApi

import play.api._
import play.api.mvc._

import models.{ User, Sticky, StickyDAO }
import scala.concurrent.ExecutionContext.Implicits.global

import com.mohiva.play.silhouette.api.{ Environment, LogoutEvent, Silhouette }
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

import java.sql.Timestamp

import play.api.libs.json._
import play.api.libs.functional.syntax._

class StickyController @Inject() (
    val messagesApi: MessagesApi,
    val env: Environment[User, JWTAuthenticator]) 
  extends Silhouette[User, JWTAuthenticator] {

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
    
  /**
   * Gets stickies by board id
   * @return HTTP response of a JSON string
   */
  def getStickies(bid: Long) = SecuredAction.async { implicit request =>
    val f = StickyDAO.findByBid(bid)

    f.map(s => Ok(Json.toJson(s)))
  }

  /**
   * Gets stickies by sticky id
   * @return HTTP response of a JSON string
   */
  def getSticky(id: Long) = SecuredAction.async { implicit request =>
    val f = StickyDAO.findById(id)

    f.map(s => Ok(Json.toJson(s)))
  }

  /**
   * Upload a new sticky through currently logged in user
   * @return HTTP response of a JSON string
   */
  def addSticky = Action(BodyParsers.parse.json) { request =>
    val stickyResult = request.body.validate[Sticky]
    stickyResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      sticky => {
        println("StickyDAO trying to save " + sticky)
        StickyDAO.create(sticky)
        Ok(Json.obj("status" -> "OK", "message" -> ("Sticky '"+sticky+"' saved.") ))
      }
    )
  }
  
  /**
   * Updates a sticky by its id
   * @param id 
   * @return HTTP response of a JSON string
   */
  def updateSticky(id: Long) = SecuredAction(BodyParsers.parse.json) { implicit request =>
    val stickyResult = request.body.validate[Sticky]
    stickyResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      sticky => {
        println("StickyDAO trying to save " + sticky)
        StickyDAO.create(sticky)
        Ok(Json.obj("status" -> "OK", "message" -> ("Sticky '"+sticky+"' updated.") ))
      }
    )
  }

  /**
   * Deletes a sticky by its id
   * @param id 
   * @return Ok response
   */
  def deleteSticky(id: Long) = SecuredAction { implicit request =>
    // TODO: if sticky does not exist
    StickyDAO.delete(id)
    Ok(Json.obj("status" -> "OK", "message" -> ("Sticky '"+id+"' deleted.") ))
  }
}