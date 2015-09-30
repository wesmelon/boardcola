package models

import java.util.UUID

import java.sql.Timestamp
import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }

/**
 * The user object.
 *
 * @param userID
 * @param providerID
 * @param providerKey
 * @param email
 * @param username
 * @param creationTime
 * @param lastLogin
 */
case class User(
  userID: UUID, 
  providerID: String,
  providerKey: String,
  email: String,
  username: String,
  creationTime: Option[Timestamp], 
  lastLogin: Option[Timestamp]) extends Identity

/**
 * Companion object
 */
object User {
  /**
   * TODO: Converts the [User] object to JSON.
   */
}
