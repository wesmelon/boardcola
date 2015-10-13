package dal

import javax.inject.{Inject, Singleton}

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.util.PasswordInfo
import com.mohiva.play.silhouette.impl.daos.DelegableAuthInfoDAO

import scala.concurrent.{ Future, ExecutionContext }
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.PostgresDriver.api._
import slick.driver.JdbcProfile

import scala.collection.mutable

case class PasswordInfoWithIdentifier(
  providerId: String,
  providerKey: String,
  hasher: String,
  password: String,
  salt: Option[String] = None
)

/**
 * The DAO to store the password information.
 */
class PasswordInfoDAO @Inject()(passwordInfoDAO: PasswordRepo) extends DelegableAuthInfoDAO[PasswordInfo] {

  /**
   * Finds the auth info which is linked with the specified login info.
   *
   * @param loginInfo The linked login info.
   * @return The retrieved auth info or None if no auth info could be retrieved for the given login info.
   */
  def find(loginInfo: LoginInfo): Future[Option[PasswordInfo]] = {
    passwordInfoDAO.findByProviderIdAndKey(loginInfo)
  }

  /**
   * Adds new auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be added.
   * @param authInfo The auth info to add.
   * @return The added auth info.
   */
  def add(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    passwordInfoDAO.insert(loginInfo, authInfo)
  }

  /**
   * Updates the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be updated.
   * @param authInfo The auth info to update.
   * @return The updated auth info.
   */
  def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    passwordInfoDAO.update(loginInfo, authInfo)
  }

  /**
   * Saves the auth info for the given login info.
   *
   * This method either adds the auth info if it doesn't exists or it updates the auth info
   * if it already exists.
   *
   * @param loginInfo The login info for which the auth info should be saved.
   * @param authInfo The auth info to save.
   * @return The saved auth info.
   */
  def save(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    find(loginInfo).flatMap {
      case Some(_) => update(loginInfo, authInfo)
      case None => add(loginInfo, authInfo)
    }
  }

  /**
   * Removes the auth info for the given login info.
   *
   * @param loginInfo The login info for which the auth info should be removed.
   * @return A future to wait for the process to be completed.
   */
  def remove(loginInfo: LoginInfo): Future[Unit] = {
    passwordInfoDAO.delete(loginInfo)
    Future.successful(())
  }
}

@Singleton
class PasswordRepo @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  // Brings db into scope
  import dbConfig._
  import driver.api._

  private class Passwords(tag: Tag) extends Table[PasswordInfoWithIdentifier](tag, "passwords") {
    def providerId = column[String]("provider_id")
    def providerKey = column[String]("provider_key")
    def hasher = column[String]("hasher")
    def password = column[String]("password")
    def salt = column[Option[String]]("salt")

    def * = (providerId, providerKey, hasher, password, salt) <> (PasswordInfoWithIdentifier.tupled, PasswordInfoWithIdentifier.unapply)
  }

  private val passwords = TableQuery[Passwords]

  def insert(loginInfo: LoginInfo, authInfo: PasswordInfo) : Future[PasswordInfo] = {
    val action = passwords.map(p => (p.providerId, p.providerKey, p.hasher, p.password, p.salt)) +=
      (loginInfo.providerID, loginInfo.providerKey, authInfo.hasher, authInfo.password, authInfo.salt)

    db.run(action)
    Future.successful(authInfo)
  }

  def findByProviderIdAndKey(loginInfo: LoginInfo) : Future[Option[PasswordInfo]] = {
    val query = passwords.filter(p => p.providerId === loginInfo.providerID && p.providerKey === loginInfo.providerKey)

    val futureResult : Future[Option[PasswordInfoWithIdentifier]] = db.run(query.result.headOption)
    futureResult.map(futurePw =>
      futurePw match {
        case Some(pw) => Some(PasswordInfo(pw.hasher, pw.password, pw.salt))
        case None => None
      }
    )
  }

  def update(loginInfo: LoginInfo, authInfo: PasswordInfo): Future[PasswordInfo] = {
    val action = passwords.filter(p => 
      p.providerId === loginInfo.providerID && 
      p.providerKey === loginInfo.providerKey)
        .map(p => (p.hasher, p.password, p.salt))
        .update(authInfo.hasher, authInfo.password, authInfo.salt)

    db.run(action)
    Future.successful(authInfo)
  }

  def delete(loginInfo: LoginInfo) = db.run {
    passwords.filter(p => 
      p.providerId === loginInfo.providerID && 
      p.providerKey === loginInfo.providerKey)
        .delete
  }
}