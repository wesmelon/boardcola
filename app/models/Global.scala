package models

import slick.driver.H2Driver.api._

object Global {
	val db = Database.forConfig("databaseUrl")
}