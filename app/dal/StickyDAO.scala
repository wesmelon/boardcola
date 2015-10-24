package dal

import javax.inject.{Inject, Singleton}
import java.time.Instant
import models.Sticky

import java.sql.Timestamp
import java.util.Calendar

import scala.concurrent.{ Future, ExecutionContext }
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.PostgresDriver.api._
import slick.driver.JdbcProfile

@Singleton
class StickyRepo @Inject() (boardDAO: BoardRepo, 
  dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  
  import dbConfig._
  import driver.api._

  private class Stickies(tag: Tag) extends Table[Sticky](tag, "stickies") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def bid = column[Long]("board_id")
    def name = column[String]("name", O.Default(""))
    def content = column[String]("content")
    def xPos = column[Int]("x", O.Default(0))
    def yPos = column[Int]("y", O.Default(0))
    def creationTime = column[Option[Timestamp]]("creation_time")
    def lastModified = column[Option[Timestamp]]("last_modified", O.Default(None))

    def * = (id.?, bid, name.?, content, xPos.?, yPos.?, creationTime, lastModified) <>
      ((Sticky.apply _).tupled, Sticky.unapply)
  }

  private val stickies = TableQuery[Stickies]

  def create(bid: Long, sticky: Sticky): Future[Sticky] = db.run {
    val timestamp = Instant.now()
    val created = new Timestamp(timestamp.toEpochMilli())
    val stickyWithTime = sticky.copy(creationTime=Some(created))

    (stickies returning stickies.map(_.id) 
      into ((sticky, id) => sticky.copy(bid=bid, id=Some(id)))
    ) += stickyWithTime
  }

  def findAll: Future[Seq[Sticky]] = db.run(stickies.result)

  def findByBid(bid: Long): Future[Seq[Sticky]] = 
    db.run(stickies.filter(_.bid === bid).result)

  def findById(bid: Long, id: Long): Future[Sticky] = 
    db.run(stickies.filter(s => s.id === id && s.bid === bid).result.head)

  def update(bid: Long, id: Long, sticky: Sticky): Future[Unit] = db.run { 
    val q = for { s <- stickies if (s.id === id && s.bid === bid) } yield s
    q.update(sticky).map(_ => ())
  }

  def delete(bid: Long, id: Long): Future[Unit] = 
    db.run(stickies.filter(s => s.id === id && s.bid === bid).delete.map(_ => ()))
}