package models.daos

import models.User
import models.dbConfig._

import java.util.UUID
import java.sql.Timestamp
import java.util.Calendar

import com.mohiva.play.silhouette.api.LoginInfo
import scala.concurrent.{ Future, ExecutionContext }
import slick.driver.PostgresDriver.api._

/**
 * Mapping of the users in the database to the User case class.
 */
class Users(tag: Tag) extends Table[User](tag, "users") {
  def userID = column[UUID]("user_id", O.PrimaryKey)
  def providerID = column[String]("provider_id")
  def providerKey = column[String]("provider_key")
  def email = column[String]("email")
  def username = column[String]("username")
  def creationTime = column[Option[Timestamp]]("creation_time")
  def lastLogin = column[Option[Timestamp]]("last_login", O.Default(None))

  def * = (userID, providerID, providerKey, email, username, creationTime, lastLogin) <> ((User.apply _).tupled, User.unapply)
}

/**
 * Give access to the user object.
 */
class UserDAOImpl extends UserDAO {
  import models.daos.UserDAOImpl._

  /**
   * Finds a user by its login info.
   *
   * @param loginInfo The login info of the user to find.
   * @return The found user or None if no user for the given login info could be found.
   */
  def findByLoginInfo(loginInfo: LoginInfo) = findByProviderIdAndKey(loginInfo.providerID, loginInfo.providerKey)

  /**
   * Finds a user by its user ID.
   *
   * @param userID The ID of the user to find.
   * @return The found user or None if no user for the given ID could be found.
   */
  def findById(id: UUID) = findById(id)

  /**
   * Creates a user.
   *
   * @param user The user to create.
   * @return The created user.
   */
  def create(user: User) = insert(user)

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) = update(user)
}

/**
 * The companion object.
 */
object UserDAOImpl {
  val users = TableQuery[Users]
  
  def insert(user: User): Future[User] = db.run {
    val calendar : Calendar = Calendar.getInstance()
    val now : java.util.Date = calendar.getTime()

    (users.map(u => (u.userID, u.providerID, u.providerKey, u.email, u.username, u.creationTime)) 
      returning users.map(_.userID)
      into ((value, userID) => User(userID, value._2, value._3, value._4, value._5, value._6, None))
    ) += (user.userID, user.providerID, user.providerKey, user.email, user.username, Some(new Timestamp(now.getTime())))
  }

  def findAll: Future[Seq[User]] = db.run { users.result }

  def findById(id: UUID): Future[Option[User]] = db.run { 
    users.filter(u => u.userID === id).result.headOption 
  }

  def findByProviderIdAndKey(id: String, key: String): Future[Option[User]] = db.run {
    users.filter(u => u.providerID === id && u.providerKey === key).result.headOption
  }

  def update(user: User): Future[User] = {
    val calendar : Calendar = Calendar.getInstance()
    val now : java.util.Date = calendar.getTime()

    val action = users.filter(_.userID === user.userID)
      .map(u => (u.providerID, u.providerKey, u.email, u.username, u.lastLogin))
      .update(user.providerID, user.providerKey, user.email, user.username, Some(new Timestamp(now.getTime())))

    db.run(action)
    Future.successful(user)
  }

  def updateEmail(id: UUID, email: String) = db.run {
    users.filter(_.userID === id)
      .map(u => u.email)
      .update(email)
  }

  def delete(id: UUID) = db.run { users.filter(_.userID === id).delete }
}