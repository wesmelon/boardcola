package controllers.api

import javax.inject.Inject
import play.api.i18n.MessagesApi

import play.api._
import play.api.mvc._

import models.{ User, Category }
import dal.CategoryRepo
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.mohiva.play.silhouette.api.{ Environment, LogoutEvent, Silhouette }
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

import java.sql.Timestamp
import java.util.UUID

import play.api.libs.json._

class CategoryController @Inject() (
    categoryDAO: CategoryRepo,
    val messagesApi: MessagesApi,
    val env: Environment[User, JWTAuthenticator]) 
  extends Silhouette[User, JWTAuthenticator] {

  /**
   * Gets categories by currently logged in user
   * @return HTTP response of a JSON string
   */
  def getCategories = SecuredAction.async { implicit request =>
    categoryDAO.findByUid(request.identity.userID).map(s => Ok(Json.toJson(s)))
  }

  /**
   * Upload a new category through currently logged in user
   * @return HTTP response of a JSON string
   */
  def addCategory = 
    SecuredAction.async(BodyParsers.parse.json) { implicit request =>

    val catResult = request.body.validate[Category]
    catResult.fold(
      errors => {
        Future.successful(BadRequest(JsError.toJson(errors)))
      },
      cat => {
        val catWithId = cat.copy(uid=Some(request.identity.userID));
        categoryDAO.create(catWithId).map(c => Created(Json.toJson(c)))
      }
    )
  }
  
  /**
   * Updates a category by its id
   * @param id 
   * @return HTTP response of a JSON string
   */
  def updateCategory(id: Long) = 
    SecuredAction.async(BodyParsers.parse.json) { implicit request =>

    val catResult = request.body.validate[Category]
    catResult.fold(
      errors => {
        Future.successful(BadRequest(JsError.toJson(errors)))
      },
      cat => {
        val categoryWithId = cat.copy(uid=Some(request.identity.userID));
        categoryDAO.update(id, categoryWithId)
        categoryDAO.findById(id).map(s => Ok(Json.toJson(s))) // hack
      }
    )
  }

  /**
   * Deletes a category by its id
   * @param id 
   * @return Ok response
   */
  def deleteCategory(id: Long) = SecuredAction.async { implicit request =>    
    categoryDAO.delete(id).map(c => Ok(Json.obj("id" -> id)))
  }
}