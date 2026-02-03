package service

import scala.concurrent.Future
import javax.inject.Inject
import play.api.libs.ws._
import scala.concurrent.ExecutionContext
import play.api.Configuration
import service.sheets.model.SheetException
import play.api.Logging

class JwtTokenExchangeService @Inject() (ws: WSClient, ec: ExecutionContext, config: Configuration) extends Logging {

    val authServiceUrl = config.get[String]("services.oauth.route")
    val cookieName = config.get[String]("services.oauth.jwt.cookie.name")
    def exchange(jwt: String): Future[String] = {
        val request: WSRequest = ws.url(authServiceUrl)
        val complexRequest: WSRequest = request.addCookies(DefaultWSCookie(cookieName, jwt, None, None, None, true, true))
        return complexRequest.get().map(r => r.status match {
            case 200 => r.body
            case _: Int => {
                logger.error(s"Code erreur: ${r.status}/${r.statusText}. Body: ${r.body}")
                throw new SheetException(s"Récupération du token invalide. Code erreur: ${r.status}/${r.statusText}")
            }
        })(ec)
    }
}