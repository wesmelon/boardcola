package models

import java.sql._
import java.util.Calendar

import scala.concurrent.Future
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

case class User(id: Option[Long], email: String, username: String, password: String, authToken: Option[String], creationTime: Timestamp, lastLogin: Option[Timestamp])

class Users(tag: Tag) extends Table[User](tag, "users") {
	def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
	def email = column[String]("email")
	def username = column[String]("username")
	def password = column[String]("password")
	def authToken = column[Option[String]]("auth_token", O.Default(None))
	def creationTime = column[Timestamp]("creation_time")
	def lastLogin = column[Option[Timestamp]]("last_login", O.Default(None))

    def * = (id.?, email, username, password, authToken, creationTime, lastLogin) <> ((User.apply _).tupled, User.unapply)
}

/*
 * This acts as the data access layer.
 */
object User {
	val users = TableQuery[Users]
	
	def create(user: User) = {
		val calendar : Calendar = Calendar.getInstance()
		val now : java.util.Date = calendar.getTime()
		val action = users.map(u => (u.email, u.username, u.password, u.creationTime)) += (user.email, user.username, user.password, new Timestamp(now.getTime()))

		Global.db.run(action)
	}

	def findAll() : Future[Seq[User]] = {
		val query = users

		val result : Future[Seq[User]] = Global.db.run(query.result)
		result
	}

	def findByEmail(email: String) : Future[User] = {
		val query = users.filter(_.email === email)
		
		val result : Future[User] = Global.db.run(query.result.head)
		result
	}

	def findByIdAndEmail(id: Long, email: String) : Future[User] = {
		val query = users.filter(u => u.id === id && u.email === email)
		
		val result : Future[User] = Global.db.run(query.result.head)
		result
	}

	def updateEmail(id: Long, email: String) = {
		val action = users.filter(_.id === id)
						  .map(u => u.email)
						  .update(email)

		Global.db.run(action)
	}

	def updatePassword(id: Long, password: String) = {
		val action = users.filter(_.id === id)
						  .map(u => u.password)
						  .update(password)

		Global.db.run(action)
	}

	def updateToken(id: Long, token: String) = {
		val action = users.filter(_.id === id)
						  .map(u => u.email)
						  .update(token)

		Global.db.run(action)
	}

	def delete(id: Long) = {
		val action = users.filter(_.id === id)
						  .delete

		Global.db.run(action)
	}
}
