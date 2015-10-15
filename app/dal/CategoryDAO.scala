package dal

import models.Category
import javax.inject.{Inject, Singleton}
import java.util.UUID

import scala.concurrent.{ Future, ExecutionContext }
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.PostgresDriver.api._
import slick.driver.JdbcProfile

@Singleton
class CategoryRepo @Inject() 
  (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  
  import dbConfig._
  import driver.api._

  private class Categories(tag: Tag) extends Table[Category](tag, "categories") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def uid = column[Option[UUID]]("user_id")
    def name = column[String]("name")

    def * = (id.?, uid, name) <> ((Category.apply _).tupled, Category.unapply)
  }

  private val categories = TableQuery[Categories]

  def create(category: Category): Future[Category] = db.run {
    (categories returning categories.map(_.id) 
      into ((category,id) => category.copy(id=Some(id)))
    ) += category
  }

  def findAll: Future[Seq[Category]] = db.run(categories.result)

  def findById(id: Long): Future[Category] = 
    db.run(categories.filter(_.id === id).result.head)

  def findByUid(uid: UUID): Future[Seq[Category]] = 
    db.run(categories.filter(_.uid === uid).result)

  def update(id: Long, cat: Category): Future[Category] = db.run {
    val q = for { c <- categories if c.id === id } yield c
    q.update(cat)

    categories.filter(_.id === id).result.head
  }

  def delete(id: Long): Future[Unit] = 
    db.run(categories.filter(_.id === id).delete.map(_ => ()))
}