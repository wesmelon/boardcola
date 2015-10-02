package controllers.api

import play.api._
import play.api.mvc._

import models._
import play.api.libs.json._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class StickyController extends Controller {
	def getAllStickies = Action.async {
		val f = Sticky.findAll()

    f.map(s => Ok(Json.toJson(s)))
	}
}