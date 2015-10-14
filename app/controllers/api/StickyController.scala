package controllers.api

import javax.inject.Inject
import play.api.i18n.MessagesApi

import play.api._
import play.api.mvc._

import models.{ User, Sticky }
import dal.StickyRepo
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.mohiva.play.silhouette.api.{ Environment, LogoutEvent, Silhouette }
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

import java.sql.Timestamp

import play.api.libs.json._

class StickyController @Inject() (
    stickyDAO: StickyRepo,
    val messagesApi: MessagesApi,
    val env: Environment[User, JWTAuthenticator]) 
  extends Silhouette[User, JWTAuthenticator] {

  /**
   * Gets stickies by board id
   * @return HTTP response of a JSON string
   */
  def getStickies(bid: Long) = SecuredAction.async { implicit request =>
    val f = stickyDAO.findByBid(bid)

    f.map(s => Ok(Json.toJson(s)))
  }

  /**
   * Gets stickies by sticky id
   * @return HTTP response of a JSON string
   */
  def getSticky(id: Long) = SecuredAction.async { implicit request =>
    val f = stickyDAO.findById(id)

    f.map(s => Ok(Json.toJson(s)))
  }

  /**
   * Upload a new sticky through currently logged in user
   * @return HTTP response of a JSON string
   */
  def addSticky = 
    SecuredAction.async(BodyParsers.parse.json) { implicit request =>

    val stickyResult = request.body.validate[Sticky]
    stickyResult.fold(
      errors => {
        Future.successful(BadRequest(JsError.toJson(errors)))
      },
      sticky => {
        stickyDAO.create(sticky).map(s => Ok(Json.toJson(s)))
      }
    )
  }
  
  /**
   * Updates a sticky by its id
   * @param id 
   * @return HTTP response of a JSON string
   */
  def updateSticky(id: Long) = 
    SecuredAction.async(BodyParsers.parse.json) { implicit request =>

    val stickyResult = request.body.validate[Sticky]
    stickyResult.fold(
      errors => {
        Future.successful(BadRequest(JsError.toJson(errors)))
      },
      sticky => {
        stickyDAO.create(sticky).map(s => Ok(Json.toJson(s)))
      }
    )
  }

  /**
   * Deletes a sticky by its id
   * @param id 
   * @return Ok response
   */
  def deleteSticky(id: Long) = SecuredAction.async { implicit request =>
    stickyDAO.delete(id).map(s => Ok(Json.obj("id" -> id)))
  }
}