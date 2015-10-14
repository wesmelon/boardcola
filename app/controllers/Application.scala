package controllers

import javax.inject.Inject
import play.api.i18n.MessagesApi

import play.api._
import play.api.mvc._

import models.User
import scala.concurrent.Future

import com.mohiva.play.silhouette.api.{ Environment, LogoutEvent, Silhouette }
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry

import play.api.libs.json._

class Application @Inject() (
    val messagesApi: MessagesApi,
    val env: Environment[User, JWTAuthenticator],
    socialProviderRegistry: SocialProviderRegistry) 
  extends Silhouette[User, JWTAuthenticator] {

  def index = Action {
    Ok(views.html.index())
  }

  /**
   * Returns the user.
   *
   * @return The result to display.
   */
  def user = SecuredAction.async { implicit request =>
    Future.successful(Ok(Json.toJson(request.identity)))
  }

  /**
   * Manages the sign out action.
   */
  def signOut = SecuredAction.async { implicit request =>
    env.eventBus.publish(LogoutEvent(request.identity, request, request2Messages))
    env.authenticatorService.discard(request.authenticator, Ok)
  }

  /**
   * Template views
   * 
   * @param template Provided template
   * @return the template
   */
  def view(template: String) = UserAwareAction { implicit request =>
    template match {
      case "home" => Ok(views.html.home())
      case "signup" => Ok(views.html.signup())
      case "login" => Ok(views.html.login(socialProviderRegistry))
      case "navigation" => Ok(views.html.navigation())
      case "list" => Ok(views.html.list())
      case "board" => Ok(views.html.board())
      case _ => NotFound
    }
  }
}

