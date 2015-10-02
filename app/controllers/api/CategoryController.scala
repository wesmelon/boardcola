package controllers.api

import play.api._
import play.api.mvc._

import models._
import play.api.libs.json._
import scala.concurrent.Future
import scala.util.{Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global

class CategoryController extends Controller {

  def getAllCategories = Action.async {
    val f = Category.findAll()

    f.map(s => Ok(Json.toJson(s)))
  }
}