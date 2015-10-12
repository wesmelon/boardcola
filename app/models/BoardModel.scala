package models

import models.daos.UserDAOImpl

import java.util.UUID
import java.sql.Timestamp
import java.util.Calendar

import scala.concurrent.{ Future, ExecutionContext }
import slick.driver.PostgresDriver.api._

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

  // Brings db into closer scope
  import dbConfig._

  def create(board: Board): Future[Board] = db.run {
    val calendar : Calendar = Calendar.getInstance()
    val now : java.util.Date = calendar.getTime()

    (boards.map(b => (b.uid, b.cid, b.name, b.creationTime))
      returning boards.map(_.id)
      into ((value, id) => Board(Some(id), value._1, value._2, value._3, value._4, None))
    ) += (board.uid, board.cid, board.name, Some(new Timestamp(now.getTime())))
  }

  def findAll: Future[Seq[Board]] = db.run { boards.result }
  def findById(id: Long): Future[Board] = db.run { boards.filter(_.id === id).result.head }
  def findByUid(uid: UUID): Future[Seq[Board]] = db.run { boards.filter(_.uid === uid).result }
  def findByCid(cid: Long): Future[Seq[Board]] = db.run { boards.filter(_.cid === cid).result }

  def updateName(id: Long, name: String) = db.run {
    boards.filter(_.id === id)
      .map(b => b.name)
      .update(name)
  }

  def delete(id: Long) = db.run { boards.filter(_.id === id).delete }
 }