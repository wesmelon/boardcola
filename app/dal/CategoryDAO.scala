package dal

import models.Category
import javax.inject.{Inject, Singleton}
import java.util.UUID

import scala.concurrent.{ Future, ExecutionContext }
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.PostgresDriver.api._
import slick.driver.JdbcProfile

@Singleton
class CategoryRepo @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  
  // Brings db into scope
  import dbConfig._
  import driver.api._

  private class Categories(tag: Tag) extends Table[Category](tag, "categories") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def uid = column[UUID]("user_id")
    def name = column[String]("name")

    def * = (id.?, uid, name) <> (Category.tupled, Category.unapply)
  }

  private val categories = TableQuery[Categories]

  def create(category: Category) = db.run {
    (categories.map(c => (c.uid, c.name)) 
      returning categories.map(_.id)
      into ((value,id) => Category(Some(id), value._1, value._2))
    ) += (category.uid, category.name)
  }

  def findAll: Future[Seq[Category]] = db.run(categories.result)
  def findById(id: Long): Future[Category] = db.run(categories.filter(_.id === id).result.head)
  def findByUid(uid: UUID): Future[Seq[Category]] = db.run(categories.filter(_.uid === uid).result)

  def update(id: Long, cat: Category) = db.run {
    categories.filter(_.id === id)
      .map(c => c.name)
      .update(cat.name)
  }

  def delete(id: Long) = db.run(categories.filter(_.id === id).delete)
}