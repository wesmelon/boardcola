package services

import java.util.UUID
import javax.inject.Inject

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.SocialProfile
import models.User
import dal.UserDAO
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

/**
 * Handles actions to users.
 *
 * @param userDAO The user DAO implementation.
 */
class UserServiceImpl @Inject() (userDAO: UserDAO) extends UserService {

  /**
   * Retrieves a user that matches the specified login info.
   *
   * @param loginInfo The login info to retrieve a user.
   * @return The retrieved user or None if no user could be retrieved for the given login info.
   */
  def retrieve(loginInfo: LoginInfo): Future[Option[User]] = userDAO.findByLoginInfo(loginInfo)

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User) = userDAO.save(user)

  /**
   * Creates a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def create(user: User) = userDAO.create(user)

  /**
   * Saves the social profile for a user.
   *
   * If a user exists for this profile then update the user, otherwise create a new user with the given profile.
   *
   * @param profile The social profile to save.
   * @return The user for whom the profile was saved.
   */
  def save(profile: BoardcolaSocialProfile) = {
    userDAO.findByLoginInfo(profile.loginInfo).flatMap {
      case Some(user) => // Update user with profile
        userDAO.save(user.copy(
          providerID = profile.loginInfo.providerID,
          providerKey = profile.loginInfo.providerKey,
          email = profile.email,
          username = profile.username,
          creationTime = None,
          lastLogin = None
        ))
      case None => // Insert a new user
        userDAO.create(User(
          userID = UUID.randomUUID(),
          providerID = profile.loginInfo.providerID,
          providerKey = profile.loginInfo.providerKey,
          email = profile.email,
          username = profile.username,
          creationTime = None,
          lastLogin = None
        ))
    }
  }
}
