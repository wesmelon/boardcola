package controllers.auth

import java.util.UUID
import javax.inject.Inject

import services.UserService
import com.mohiva.play.silhouette.api._
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.services.AvatarService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.SignUpForm
import models.User
import play.api.i18n.{ MessagesApi, Messages }
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import play.api.mvc.Action

import scala.concurrent.Future

/**
 * The sign up controller.
 *
 * @param messagesApi The Play messages API.
 * @param env The Silhouette environment.
 * @param userService The user service implementation.
 * @param authInfoRepository The auth info repository implementation.
 * @param passwordHasher The password hasher implementation.
 */
class SignUp @Inject() (
    val messagesApi: MessagesApi,
    val env: Environment[User, JWTAuthenticator],
    userService: UserService,
    authInfoRepository: AuthInfoRepository,
    passwordHasher: PasswordHasher)
  extends Silhouette[User, JWTAuthenticator] {

  /**
   * Registers a new user.
   *
   * @return The result to display.
   */
  def signUp = Action.async(parse.json) { implicit request =>
    request.body.validate[SignUpForm.Data].map { data =>
      val loginInfo = LoginInfo(CredentialsProvider.ID, data.email)
      userService.retrieve(loginInfo).flatMap {
        case Some(user) =>
          Future.successful(BadRequest(Json.obj("message" -> Messages("user.exists"))))
        case None =>
          val authInfo = passwordHasher.hash(data.password)
          val user = User(
            userID = UUID.randomUUID(),
            providerID = loginInfo.providerID,
            providerKey = loginInfo.providerKey,
            email = data.email,
            username = data.username,
            creationTime = None,
            lastLogin = None
          )
          for {
            user <- userService.create(user)
            authInfo <- authInfoRepository.add(loginInfo, authInfo)
            authenticator <- env.authenticatorService.create(loginInfo)
            token <- env.authenticatorService.init(authenticator)
          } yield {
            env.eventBus.publish(SignUpEvent(user, request, request2Messages))
            env.eventBus.publish(LoginEvent(user, request, request2Messages))
            Ok(Json.obj("token" -> token))
          }
      }
    }.recoverTotal {
      case error =>
        Future.successful(Unauthorized(Json.obj("message" -> Messages("invalid.data"))))
    }
  }
}
