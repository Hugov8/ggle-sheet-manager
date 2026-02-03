package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.http.MediaType
import play.api.libs.json.JsValue
import service.JwtTokenExchangeService


case class JwtTokenCookieRequest[A](val jwtToken: String, request: Request[A]) extends WrappedRequest(request)

class JwtTokenPresenceHeaderAction @Inject() (val parser: BodyParsers.Default, val jwtService: JwtTokenExchangeService) (implicit val executionContext: ExecutionContext) 
    extends ActionBuilder[JwtTokenCookieRequest, AnyContent] {

  def logger = Logger(getClass)

  override def invokeBlock[A](request: Request[A],
                            block: JwtTokenCookieRequest[A] => Future[Result]): Future[Result] = {
    val token: Option[String] = request.cookies.get(jwtService.cookieName).map(_.value);
    token match {
      case Some(jwtToken) => jwtService.exchange(jwtToken)
                                .flatMap(t => block(JwtTokenCookieRequest(t, request)))
                                .recover {
                                    case e => 
                                        logger.warn("JWT invalide", e)
                                        Results.Unauthorized(Json.obj("state"->"Authentication failed"))
                                }
      case _ => 
        logger.warn(s"Connexion pour la requete a echoue : $request")
        Future.successful(Results.Unauthorized(Json.obj("state"->"Authentication failed")))
    }
  }
}
