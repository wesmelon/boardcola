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
class BoardRepo @Inject() 
  (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

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

    def * = (id.?, uid, cid, name, creationTime, lastModified) <>
     ((Board.apply _).tupled, Board.unapply)
  }

  private val boards = TableQuery[Boards]

  def create(board: Board): Future[Board] = db.run {
    val timestamp = Instant.now()
    val created = new Timestamp(timestamp.toEpochMilli())
    val boardWithTime = board.copy(creationTime=Some(created))

    (boards returning boards.map(_.id)
      into ((board, id) => board.copy(id=Some(id)))
    ) += boardWithTime
  }

  def findAll: Future[Seq[Board]] = db.run(boards.result)

  def findById(id: Long): Future[Board] = 
    db.run(boards.filter(_.id === id).result.head)

  def findByUid(uid: UUID): Future[Seq[Board]] = 
    db.run(boards.filter(_.uid === uid).result)

  def findByCid(cid: Long): Future[Seq[Board]] = 
    db.run(boards.filter(_.cid === cid).result)

  def update(id: Long, board: Board): Future[Unit] = db.run {
    val q = for { b <- boards if b.id === id } yield b
    q.update(board).map(_ => ())
  }

  def delete(id: Long): Future[Unit] = 
    db.run(boards.filter(_.id === id).delete.map(_ => ()))
}