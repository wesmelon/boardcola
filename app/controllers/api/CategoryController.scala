package controllers.api

import javax.inject.Inject
import play.api.i18n.MessagesApi

import play.api._
import play.api.mvc._

import models._
import scala.concurrent.Future
import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global

import com.mohiva.play.silhouette.api.{ Environment, LogoutEvent, Silhouette }
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

import java.sql.Timestamp
import java.util.UUID

import play.api.libs.json._
import play.api.libs.functional.syntax._

class CategoryController @Inject() (
    val messagesApi: MessagesApi,
    val env: Environment[User, JWTAuthenticator]) 
  extends Silhouette[User, JWTAuthenticator] {

  implicit val categoryWrites = new Writes[Category] {
      def writes(category: Category) = Json.obj(
        "id" -> category.id,
        "uid" -> category.uid,
        "name" -> category.name
    )
  }

  implicit val categoryReads: Reads[Category] = (
    (JsPath \ "id").read[Long] and
    (JsPath \ "uid").read[UUID] and
    (JsPath \ "name").read[String]
  )(Category.apply _)

  /**
   * Gets categories by currently logged in user
   * @return HTTP response of a JSON string
   */
  def getCategories = SecuredAction.async { implicit request =>
    val f = CategoryDAO.findByUid(request.identity.userID)

    f.map(s => Ok(Json.toJson(s)))
  }

  /**
   * Upload a new category through currently logged in user
   * @return HTTP response of a JSON string
   */
  def addCategory = Action(BodyParsers.parse.json) { request =>
    val catResult = request.body.validate[Category]
    catResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      cat => {
        println("CategoryDAO trying to save " + cat)
        CategoryDAO.create(cat)
        Ok(Json.obj("status" -> "OK", "message" -> ("Category '"+cat+"' saved.") ))
      }
    )
  }
  
  /**
   * Updates a category by its id
   * @param id 
   * @return HTTP response of a JSON string
   */
  def updateCategory(id: Long) = Action(BodyParsers.parse.json) { request =>
    val catResult = request.body.validate[Category]
    catResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(errors)))
      },
      cat => {
        println("CategoryDAO trying to update " + cat)
        CategoryDAO.update(id, cat)
        Ok(Json.obj("status" -> "OK", "message" -> ("Category '"+cat+"' updated.") ))
      }
    )
  }

  /**
   * Deletes a category by its id
   * @param id 
   * @return Ok response
   */
  def deleteCategory(id: Long) = Action { implicit request =>
    // TODO: if category does not exist
    CategoryDAO.delete(id)
    Ok(Json.obj("status" -> "OK", "message" -> ("Category '"+id+"' deleted.") ))
  }
}