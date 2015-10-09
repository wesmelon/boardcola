package models

import java.util.UUID
import models.daos.UserDAOImpl

import java.sql.Timestamp
import java.util.Calendar

import scala.concurrent.Future
import slick.driver.PostgresDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Board(
  id: Option[Long], 
  uid: Option[UUID], 
  cid: Option[Long], 
  name: String, 
  creationTime: Option[Timestamp], 
  lastModified: Option[Timestamp])

class Boards(tag: Tag) extends Table[Board](tag, "boards") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def uid = column[Option[UUID]]("user_id")
  def cid = column[Option[Long]]("category_id")
  def name = column[String]("name")
  def creationTime = column[Option[Timestamp]]("creation_time")
  def lastModified = column[Option[Timestamp]]("last_modified", O.Default(None))

  def * = (id.?, uid, cid, name, creationTime, lastModified) <> (Board.tupled, Board.unapply)
  def user = foreignKey("u_fk", uid, UserDAOImpl.users)(_.userID.?, onDelete=ForeignKeyAction.Cascade)
}

/*
 * This acts as the data access layer.
 */
 object BoardDAO {
  val boards = TableQuery[Boards]

  def create(board: Board) = {
    val calendar : Calendar = Calendar.getInstance()
    val now : java.util.Date = calendar.getTime()
    val action = boards.map(b => (b.uid, b.cid, b.name, b.creationTime)) += (board.uid, board.cid, board.name, Some(new Timestamp(now.getTime())))

    Global.db.run(action)
  }

  def findAll(): Future[Seq[Board]] = {
    val query = boards

    val result : Future[Seq[Board]] = Global.db.run(query.result)
    result
  }

  def findById(id: Long): Future[Board] = {
    val query = boards.filter(_.id === id)

    val result : Future[Board] = Global.db.run(query.result.head)
    result
  }

  def findByUid(uid: UUID): Future[Seq[Board]] = {
    val query = boards.filter(_.uid === uid)

    val result : Future[Seq[Board]] = Global.db.run(query.result)
    result
  }

  def findByCid(cid: Long): Future[Seq[Board]] = {
    val query = boards.filter(_.cid === cid)

    val result : Future[Seq[Board]] = Global.db.run(query.result)
    result
  }

  def updateName(id: Long, name: String) = {
    val action = boards.filter(_.id === id)
      .map(b => b.name)
               .update(name)

    Global.db.run(action)
  }

  def delete(id: Long) = {
    val action = boards.filter(_.id === id)
      .delete

    Global.db.run(action)
  }
 }