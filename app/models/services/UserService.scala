package models.services

import java.util.UUID
import java.sql.Timestamp
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.impl.providers.SocialProfile
import models.User

import scala.concurrent.Future

case class BoardcolaSocialProfile(
  loginInfo: LoginInfo,
  userId: Option[UUID], 
  email: String,
  username: String,
  creationTime: Option[Timestamp], 
  lastLogin: Option[Timestamp]) extends SocialProfile

/**
 * Handles actions to users.
 */
trait UserService extends IdentityService[User] {

  /**
   * Saves a user.
   *
   * @param user The user to save.
   * @return The saved user.
   */
  def save(user: User): Future[User]

  /**
   * Saves the social profile for a user.
   *
   * If a user exists for this profile then update the user, otherwise create a new user with the given profile.
   *
   * @param profile The social profile to save.
   * @return The user for whom the profile was saved.
   */
  def save(profile: BoardcolaSocialProfile): Future[User]
}