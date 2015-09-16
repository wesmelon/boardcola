package models.daos

import models.User
import models.daos.UserDAOImpl._
import models.Global

import java.util.UUID
import java.sql.Timestamp
import java.util.Calendar

import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

import com.mohiva.play.silhouette.api.LoginInfo
import scala.concurrent.Future

/**
 * Mapping of the users in the database to the User case class.
 */
class Users(tag: Tag) extends Table[User](tag, "users") {
  def userId = column[UUID]("id", O.PrimaryKey, O.AutoInc)
  def providerId = column[String]("provider_id")
  def providerKey = column[String]("provider_key")
  def email = column[String]("email")
  def username = column[String]("username")
  def creationTime = column[Option[Timestamp]]("creation_time")
  def lastLogin = column[Option[Timestamp]]("last_login", O.Default(None))

  def * = (userId.?, providerId, providerKey, email, username, creationTime, lastLogin) <> ((User.apply _).tupled, User.unapply)
}

/**
 * Give access to the user object.
 */
class UserDAOImpl extends UserDAO {
  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def findByLoginInfo(loginInfo: LoginInfo) = {
    findByProviderIdAndKey(loginInfo.providerID, loginInfo.providerKey)
  }

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def findById(id: UUID) = {
    findById(id)
  }

  /**
   * Creates a user.
   *
   * @param user The user to create.
   * @return The created user.
   */
  def create(user: User) = {
    insert(user)
  }

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) = {
    update(user)
  }
}

/**
 * The companion object.
 */
object UserDAOImpl {
  val users = TableQuery[Users]
  
  def insert(user: User) : Future[User] = {
    val calendar : Calendar = Calendar.getInstance()
    val now : java.util.Date = calendar.getTime()
    val action = users.map(u => (u.providerId, u.providerKey, u.email, u.username, u.creationTime)) += 
      (user.providerId, user.providerKey, user.email, user.username, Some(new Timestamp(now.getTime())))

    Global.db.run(action)
    Future.successful(user)
  }

  def findAll() : Future[Seq[User]] = {
    val query = users

    val result : Future[Seq[User]] = Global.db.run(query.result)
    result
  }

  def findById(id: UUID) : Future[Option[User]] = {
    val query = users.filter(u => u.userId === id)
    
    val result : Future[Option[User]] = Global.db.run(query.result.headOption)
    result
  }

  def findByProviderIdAndKey(id: String, key: String) : Future[Option[User]] = {
    val query = users.filter(u => u.providerId === id && u.providerKey === key)
    
    val result : Future[Option[User]] = Global.db.run(query.result.headOption)
    result
  }

  def update(user: User) : Future[User] = {
    val calendar : Calendar = Calendar.getInstance()
    val now : java.util.Date = calendar.getTime()

    val action = users.filter(_.userId === user.userId)
      .map(u => (u.providerId, u.providerKey, u.email, u.username, u.lastLogin))
      .update(user.providerId, user.providerKey, user.email, user.username, Some(new Timestamp(now.getTime())))

    Global.db.run(action)
    Future.successful(user)
  }

  def updateEmail(id: UUID, email: String) = {
    val action = users.filter(_.userId === id)
      .map(u => u.email)
      .update(email)

    Global.db.run(action)
  }

  def delete(id: UUID) = {
    val action = users.filter(_.userId === id)
      .delete

    Global.db.run(action)
  }
}