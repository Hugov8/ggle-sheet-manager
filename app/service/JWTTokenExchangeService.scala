package service

import scala.concurrent.Future
import javax.inject.Inject
import play.api.libs.ws._
import scala.concurrent.ExecutionContext
import play.api.Configuration

class JwtTokenExchangeService @Inject() (ws: WSClient, ec: ExecutionContext, config: Configuration) {

    val authServiceUrl = config.get[String]("services.oauth.route")
    def exchange(jwt: String): Future[String] = {
        val request: WSRequest = ws.url(authServiceUrl)
        val complexRequest: WSRequest = request.addCookies(DefaultWSCookie("JWT", jwt))
        return complexRequest.get().map(r => r.body)(ec)
    }
}