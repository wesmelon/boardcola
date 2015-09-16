package models

import java.util.UUID
import java.sql.Timestamp
import java.util.Calendar

import scala.concurrent.Future
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

case class Board(id: Option[UUID], cid: UUID, name: String, creationTime: Timestamp, lastModified: Option[Timestamp])

class Boards(tag: Tag) extends Table[Board](tag, "boards") {
	def id = column[UUID]("id", O.PrimaryKey, O.AutoInc)
	def cid = column[UUID]("category_id")
	def name = column[String]("name")
	def creationTime = column[Timestamp]("creation_time")
	def lastModified = column[Option[Timestamp]]("last_modified", O.Default(None))

	def * = (id.?, cid, name, creationTime, lastModified) <> ((Board.apply _).tupled, Board.unapply)
	def category = foreignKey("c_fk", cid, Category.categories)(_.id, onDelete=ForeignKeyAction.Cascade)
}

/*
 * This acts as the data access layer.
 */
 object Board {
 	val boards = TableQuery[Boards]

 	def create(board: Board) = {
		val calendar : Calendar = Calendar.getInstance()
		val now : java.util.Date = calendar.getTime()
 		val action = boards.map(b => (b.cid, b.name, b.creationTime)) += (board.cid, board.name, new Timestamp(now.getTime()))

 		Global.db.run(action)
 	}

 	def findAll() : Future[Seq[Board]] = {
 		val query = boards

 		val result : Future[Seq[Board]] = Global.db.run(query.result)
 		result
 	}

 	def findById(id: UUID) : Future[Board] = {
 		val query = boards.filter(_.id === id)

 		val result : Future[Board] = Global.db.run(query.result.head)
 		result
 	}

 	def findByCid(cid: UUID) : Future[Seq[Board]] = {
 		val query = boards.filter(_.cid === cid)

 		val result : Future[Seq[Board]] = Global.db.run(query.result)
 		result
 	}

 	def updateName(id: UUID, name: String) = {
 		val action = boards.filter(_.id === id)
      .map(b => b.name)
 						   .update(name)

 		Global.db.run(action)
 	}

 	def delete(id: UUID) = {
 		val action = boards.filter(_.id === id)
      .delete

 		Global.db.run(action)
 	}
 }