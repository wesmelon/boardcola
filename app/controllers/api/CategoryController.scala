package controllers.api

import javax.inject.Inject
import play.api.i18n.MessagesApi

import play.api._
import play.api.mvc._

import models.{ User, Category }
import dal.CategoryRepo
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
    val f = categoryDAO.findByUid(request.identity.userID)

    f.map(s => Ok(Json.toJson(s)))
  }

  /**
   * Upload a new category through currently logged in user
   * @return HTTP response of a JSON string
   */
  def addCategory = SecuredAction(BodyParsers.parse.json) { implicit request =>
    val catResult = request.body.validate[Category]
    catResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      cat => {
        categoryDAO.create(cat.copy(uid=Some(request.identity.userID)))
        Ok(Json.obj("status" -> "OK", "message" -> ("Category '"+cat+"' saved.") ))
      }
    )
  }
  
  /**
   * Updates a category by its id
   * @param id 
   * @return HTTP response of a JSON string
   */
  def updateCategory(id: Long) = SecuredAction(BodyParsers.parse.json) { implicit request =>
    val catResult = request.body.validate[Category]
    catResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      cat => {
        categoryDAO.update(id, cat)
        Ok(Json.obj("status" -> "OK", "message" -> ("Category '"+cat+"' updated.") ))
      }
    )
  }

  /**
   * Deletes a category by its id
   * @param id 
   * @return Ok response
   */
  def deleteCategory(id: Long) = SecuredAction { implicit request =>
    // TODO: if category does not exist
    categoryDAO.delete(id)
    Ok(Json.obj("status" -> "OK", "message" -> ("Category '"+id+"' deleted.") ))
  }
}