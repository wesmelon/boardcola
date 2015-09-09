package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

import models._
import slick.driver.H2Driver.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Application extends Controller {
	def index = Action {
 	   	Ok(views.html.index("Your new application is ready."))
  	}
}

