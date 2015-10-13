package dal

import javax.inject.{Inject, Singleton}
import java.time.Instant
import models.Board

import java.util.UUID
import java.sql.Timestamp
import java.util.Calendar

import scala.concurrent.{ Future, ExecutionContext }
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.PostgresDriver.api._
import slick.driver.JdbcProfile

@Singleton
class BoardRepo @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  
  import dbConfig._
  import driver.api._

  private class Boards(tag: Tag) extends Table[Board](tag, "boards") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def uid = column[Option[UUID]]("user_id")
    def cid = column[Option[Long]]("category_id")
    def name = column[String]("name")
    def creationTime = column[Option[Timestamp]]("creation_time")
    def lastModified = column[Option[Timestamp]]("last_modified", O.Default(None))

    def * = (id.?, uid, cid, name, creationTime, lastModified) <> (Board.tupled, Board.unapply)
  }

  private val boards = TableQuery[Boards]

  def create(board: Board): Future[Board] = db.run {
    val timestamp = Instant.now()
    val created = new Timestamp(timestamp.toEpochMilli())

    (boards.map(b => (b.uid, b.cid, b.name, b.creationTime))
      returning boards.map(_.id)
      into ((value, id) => Board(Some(id), value._1, value._2, value._3, value._4, None))
    ) += (board.uid, board.cid, board.name, Some(created))
  }

  def findAll: Future[Seq[Board]] = db.run(boards.result)
  def findById(id: Long): Future[Board] = db.run(boards.filter(_.id === id).result.head)
  def findByUid(uid: UUID): Future[Seq[Board]] = db.run(boards.filter(_.uid === uid).result)
  def findByCid(cid: Long): Future[Seq[Board]] = db.run(boards.filter(_.cid === cid).result)

  def updateName(id: Long, name: String) = db.run {
    boards.filter(_.id === id)
      .map(b => b.name)
      .update(name)
  }

  def delete(id: Long) = db.run(boards.filter(_.id === id).delete)
}