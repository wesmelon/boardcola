package controllers.api

import javax.inject.Inject
import play.api.i18n.MessagesApi

import play.api._
import play.api.mvc._

import models.{ User, Board }
import dal.BoardRepo
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.mohiva.play.silhouette.api.{ Environment, LogoutEvent, Silhouette }
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

import java.util.UUID
import java.sql.Timestamp

import play.api.libs.json._

class BoardController @Inject() (
    boardDAO: BoardRepo,
    val messagesApi: MessagesApi,
    val env: Environment[User, JWTAuthenticator]) 
  extends Silhouette[User, JWTAuthenticator] {

 /**
   * Gets boards by currently logged in user
   * @return HTTP response of a JSON string
   */
  def getBoards = SecuredAction.async { implicit request =>
    boardDAO.findByUid(request.identity.userID).map(s => Ok(Json.toJson(s)))
  }

  /**
   * Gets boards by category id
   * @return HTTP response of a JSON string
   */
  def getBoardsByCat(cid: Long) = SecuredAction.async { implicit request =>
    boardDAO.findByCid(cid).map(s => Ok(Json.toJson(s)))
  }

  /**
   * Gets boards by board id
   * @return HTTP response of a JSON string
   */
  def getBoard(id: Long) = SecuredAction.async { implicit request =>
    boardDAO.findById(id).map(s => Ok(Json.toJson(s)))
  }

  /**
   * Upload a new board through currently logged in user
   * @return HTTP response of a JSON string
   */
  def addBoard = 
    SecuredAction.async(BodyParsers.parse.json) { implicit request =>

    val boardResult = request.body.validate[Board]
    boardResult.fold(
      errors => {
        Future.successful(BadRequest(JsError.toJson(errors)))
      },
      board => {
        val boardWithId = board.copy(uid=Some(request.identity.userID));
        boardDAO.create(boardWithId).map(b => Created(Json.toJson(b)))
      }
    )
  }
  
  /**
   * Updates a board by its id
   * @param id 
   * @return HTTP response of a JSON string
   */
  def updateBoard(id: Long) = 
    SecuredAction.async(BodyParsers.parse.json) { implicit request =>

    val boardResult = request.body.validate[Board]
    boardResult.fold(
      errors => {
        Future.successful(BadRequest(JsError.toJson(errors)))
      },
      board => {
        val boardWithId = board.copy(uid=Some(request.identity.userID));
        boardDAO.update(id, boardWithId)
        boardDAO.findById(id).map(s => Ok(Json.toJson(s))) // hack
      }
    )
  }

  /**
   * Deletes a board by its id
   * @param id 
   * @return Ok response
   */
  def deleteBoard(id: Long) = SecuredAction.async { implicit request =>
    boardDAO.delete(id).map(b => Ok(Json.obj("id" -> id)))
  }
}