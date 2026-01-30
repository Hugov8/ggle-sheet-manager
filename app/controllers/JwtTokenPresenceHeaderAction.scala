package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.http.MediaType
import play.api.libs.json.JsValue


case class JwtTokenCookieRequest[A](val jwtToken: String, request: Request[A]) extends WrappedRequest(request)

class JwtTokenPresenceHeaderAction @Inject() (val parser: BodyParsers.Default) (implicit val executionContext: ExecutionContext) extends ActionBuilder[JwtTokenCookieRequest, AnyContent] {

  def logger = Logger(getClass)

  override def invokeBlock[A](request: Request[A],
                            block: JwtTokenCookieRequest[A] => Future[Result]): Future[Result] = {
    val token: Option[String] = request.cookies.get("JWT").map(_.value);
    token match {
      case Some(jwtToken) => block(JwtTokenCookieRequest(jwtToken, request))
      case _ => 
        logger.info(s"Connexion pour la requete a echoue : $request")
        Future.successful(Results.Forbidden(Json.obj("state"->"Authentication failed")))
    }
  }
}
