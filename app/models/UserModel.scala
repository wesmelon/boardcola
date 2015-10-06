package models

import java.util.UUID

import java.sql.Timestamp
import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }

import play.api.libs.json._

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

  implicit val userWrites = new Writes[User] {
      def writes(user: User) = Json.obj(
        "id" -> user.userID,
        "providerID" -> user.providerID,
        "providerKey" -> user.providerKey,
        "email" -> user.email,
        "username" -> user.username,
        "creationTime" -> user.creationTime,
        "lastLogin" -> user.lastLogin
    )
  }
}
