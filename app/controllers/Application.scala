package controllers

import javax.inject.Inject

import play.api._
import play.api.mvc._

import models.User
import play.api.i18n.MessagesApi
import scala.concurrent.Future

import com.mohiva.play.silhouette.api.{ Environment, LogoutEvent, Silhouette }
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry

class Application @Inject() (
    val messagesApi: MessagesApi,
    val env: Environment[User, JWTAuthenticator],
    socialProviderRegistry: SocialProviderRegistry) 
  extends Silhouette[User, JWTAuthenticator] {
  
  /**
   * Template views
   * 
   * @param template Provided template
   * @return the template
   */
   def view(template: String) = UserAwareAction { implicit request =>
      template match {
        case "home" => Ok(views.html.home())
        case "signUp" => Ok(views.html.signUp())
        case "signIn" => Ok(views.html.signIn(socialProviderRegistry))
        case "navigation" => Ok(views.html.navigation())
        case _ => NotFound
      }
   }
}

