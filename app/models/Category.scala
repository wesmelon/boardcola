package models

import java.util.UUID
import models.daos.UserDAOImpl

import scala.concurrent.Future
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

case class Category(id: Long, uid: UUID, name: String)

class Categories(tag: Tag) extends Table[Category](tag, "categories") {
	def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
	def uid = column[UUID]("user_id")
	def name = column[String]("name")

  def * = (id, uid, name) <> (Category.tupled, Category.unapply)
	def user = foreignKey("u_fk", uid, UserDAOImpl.users)(_.userID, onDelete=ForeignKeyAction.Cascade)
}

/**
 * This acts as the data access layer.
 */
object CategoryDAO {
	val categories = TableQuery[Categories]

	/**
	 * TODO: Add an OK or not OK
	 */
	def create(category: Category) = {
		val action = categories.map(c => (c.uid, c.name)) += (category.uid, category.name)

		Global.db.run(action)
	}

	def findAll(): Future[Seq[Category]] = {
		val query = categories
		
		val result : Future[Seq[Category]] = Global.db.run(query.result)
		result
	}

	def findById(id: Long): Future[Category] = {
		val query = categories.filter(_.id === id)
						 	  
		val result : Future[Category] = Global.db.run(query.result.head)
		result
	}

	def findByUid(uid: UUID): Future[Seq[Category]] = {
		val query = categories.filter(_.uid === uid)
		
		val result : Future[Seq[Category]] = Global.db.run(query.result)
		result
	}

	def updateName(id: Long, name: String) = {
		val action = categories.filter(_.id === id)
      .map(c => c.name)
      .update(name)

		Global.db.run(action)
	}

	def delete(id: Long) = {
		val action = categories.filter(_.id === id)
		  .delete

		Global.db.run(action)
	}
}