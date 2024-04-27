package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.http.MediaType
import play.api.libs.json.JsValue

case class AuthenticatedUser[A](val token: String, request: Request[A]) extends WrappedRequest(request)

class AuthAction @Inject() (val parser: BodyParsers.Default)
                            (implicit val executionContext: ExecutionContext) 
                            extends ActionBuilder[AuthenticatedUser, AnyContent] {

  
  def logger = Logger(getClass)
  val PASSWORD = sys.env.get("TOKEN_API") match {
        case Some(value) => value
        case None => "key"
    }

  override def invokeBlock[A](request: Request[A],
                            block: AuthenticatedUser[A] => Future[Result]): Future[Result] = {
    val password: Option[String] = request.headers.get("token")
    password match {
      case Some(PASSWORD) => block(AuthenticatedUser(PASSWORD, request))
      case _ => 
        logger.info(s"Connexion pour la requete a echoue : $request")
        Future.successful(Results.Forbidden(Json.obj("state"->"Authentication failed")))
    }
  }
}
