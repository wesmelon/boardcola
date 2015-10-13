package controllers.api

import javax.inject.Inject
import play.api.i18n.MessagesApi

import play.api._
import play.api.mvc._

import models.{ User, Board }
import dal.BoardRepo
import scala.concurrent.ExecutionContext.Implicits.global

import com.mohiva.play.silhouette.api.{ Environment, LogoutEvent, Silhouette }
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

import java.util.UUID
import java.sql.Timestamp

import play.api.libs.json._
import play.api.libs.functional.syntax._

class BoardController @Inject() (
    boardDAO: BoardRepo,
    val messagesApi: MessagesApi,
    val env: Environment[User, JWTAuthenticator]) 
  extends Silhouette[User, JWTAuthenticator] {

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

 /**
   * Gets boards by currently logged in user
   * @return HTTP response of a JSON string
   */
  def getBoards = SecuredAction.async { implicit request =>
    val f = boardDAO.findByUid(request.identity.userID)

    f.map(s => Ok(Json.toJson(s)))
  }

  /**
   * Gets boards by category id
   * @return HTTP response of a JSON string
   */
  def getBoardsByCat(cid: Long) = SecuredAction.async { implicit request =>
    val f = boardDAO.findByCid(cid)

    f.map(s => Ok(Json.toJson(s)))
  }

  /**
   * Gets boards by board id
   * @return HTTP response of a JSON string
   */
  def getBoard(id: Long) = SecuredAction.async { implicit request =>
    val f = boardDAO.findById(id)

    f.map(s => Ok(Json.toJson(s)))
  }

  /**
   * Upload a new board through currently logged in user
   * @return HTTP response of a JSON string
   */
  def addBoard = SecuredAction(BodyParsers.parse.json) { implicit request =>
    val boardResult = request.body.validate[Board]
    boardResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      board => {
        boardDAO.create(board.copy(uid=Some(request.identity.userID)))
        Ok(Json.obj("status" -> "OK", "message" -> ("Board '"+board+"' saved.") ))
      }
    )
  }
  
  /**
   * Updates a board by its id
   * @param id 
   * @return HTTP response of a JSON string
   */
  def updateBoard(id: Long) = SecuredAction(BodyParsers.parse.json) { implicit request =>
    val boardResult = request.body.validate[Board]
    boardResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      board => {
        boardDAO.create(board)
        Ok(Json.obj("status" -> "OK", "message" -> ("Board '"+board+"' updated.") ))
      }
    )
  }

  /**
   * Deletes a board by its id
   * @param id 
   * @return Ok response
   */
  def deleteBoard(id: Long) = SecuredAction { implicit request =>
    // TODO: if board does not exist
    boardDAO.delete(id)
    Ok(Json.obj("status" -> "OK", "message" -> ("Board '"+id+"' deleted.") ))
  }
}