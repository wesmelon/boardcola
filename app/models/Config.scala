package models

import slick.driver.PostgresDriver.api._

object dbConfig {
  val db = Database.forConfig("databaseUrl")
}