package models

import java.sql.Timestamp
import java.util.Calendar

import scala.concurrent.{ Future, ExecutionContext }
import slick.driver.PostgresDriver.api._

case class Sticky(
  id: Option[Long], 
  bid: Long, 
  name: Option[String], 
  content: String, 
  xPos: Option[Int], 
  yPos: Option[Int], 
  creationTime: Option[Timestamp], 
  lastModified: Option[Timestamp])

class Stickies(tag: Tag) extends Table[Sticky](tag, "stickies") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def bid = column[Long]("board_id")
  def name = column[String]("name", O.Default(""))
  def content = column[String]("content")
  def xPos = column[Int]("x", O.Default(0))
  def yPos = column[Int]("y", O.Default(0))
  def creationTime = column[Option[Timestamp]]("creation_time")
  def lastModified = column[Option[Timestamp]]("last_modified", O.Default(None))

  def * = (id.?, bid, name.?, content, xPos.?, yPos.?, creationTime, lastModified) <> (Sticky.tupled, Sticky.unapply)
  def board = foreignKey("b_fk", bid, BoardDAO.boards)(_.id, onDelete=ForeignKeyAction.Cascade)
}

/*
 * This acts as the data access layer.
 */
object StickyDAO {
  private val stickies = TableQuery[Stickies]

  // Brings db into closer scope
  import dbConfig._
  
  def create(sticky: Sticky): Future[Sticky] = db.run {
    val calendar : Calendar = Calendar.getInstance()
    val now : java.util.Date = calendar.getTime()

    (stickies.map(s => (s.bid, s.content, s.creationTime))
      returning stickies.map(_.id)
      into ((value, id) => Sticky(Some(id), value._1, None, value._2, None, None, value._3, None))
    ) += (sticky.bid, sticky.content, Some(new Timestamp(now.getTime())))
  }

  def findAll: Future[Seq[Sticky]] = db.run { stickies.result }

  def findById(id: Long): Future[Sticky] = db.run { stickies.filter(_.id === id).result.head }

  def findByBid(bid: Long): Future[Seq[Sticky]] = db.run { stickies.filter(_.bid === bid).result }

  def update(id: Long, sticky: Sticky) = db.run {
    (stickies.filter(_.id === id)
      .map(b => (b.name, b.content))
      .update((sticky.name.get, sticky.content)))
  }

  def delete(id: Long) = { stickies.filter(_.id === id).delete }	
}