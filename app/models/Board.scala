package models

import java.sql.Timestamp
import java.util.Calendar

import scala.concurrent.Future
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.libs.json._

case class Board(id: Option[Int], cid: Int, name: String, creationTime: Timestamp, lastModified: Option[Timestamp])

class Boards(tag: Tag) extends Table[Board](tag, "boards") {
	def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
	def cid = column[Int]("category_id")
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

	implicit val boardWrites = new Writes[Board] {
	    def writes(board: Board) = Json.obj(
		    "id" -> board.id,
		    "cid" -> board.cid,
		    "name" -> board.name,
		    "creationTime" -> board.creationTime,
		    "lastModified" -> board.lastModified
		)
  }

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

 	def findById(id: Int) : Future[Board] = {
 		val query = boards.filter(_.id === id)

 		val result : Future[Board] = Global.db.run(query.result.head)
 		result
 	}

 	def findByCid(cid: Int) : Future[Seq[Board]] = {
 		val query = boards.filter(_.cid === cid)

 		val result : Future[Seq[Board]] = Global.db.run(query.result)
 		result
 	}

 	def updateName(id: Int, name: String) = {
 		val action = boards.filter(_.id === id)
      .map(b => b.name)
 						   .update(name)

 		Global.db.run(action)
 	}

 	def delete(id: Int) = {
 		val action = boards.filter(_.id === id)
      .delete

 		Global.db.run(action)
 	}
 }