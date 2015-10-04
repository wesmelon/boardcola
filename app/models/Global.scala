package models

import java.sql.Timestamp
import slick.driver.PostgresDriver.api._

import play.api.libs.json._
import play.api.libs.functional.syntax._

object Global {
	val db = Database.forConfig("databaseUrl")
}