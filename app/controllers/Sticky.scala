package controllers

import play.api._
import play.api.mvc._

import models._
import play.api.libs.json._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Sticky extends Controller {
	def getAllStickies = Action.async {
		val stickies = Sticky.findAll()
		val futureVectorOfStickiesInJson = 
			stickies.map(seq => 			// stickies is a Future[Seq[Sticky]]
				seq.map(sticky =>			// using map on the Seq, we will iterate through each Sticky
					Json.toJson(sticky)		// and convert it to a Json value
				)
			)

		val futureJsArrayofStickiesInJson = futureVectorOfStickiesInJson.map(
			v => Json.toJson(v)
		)

		futureJsArrayofStickiesInJson.map(jsArr => Ok(jsArr))
	}
}