package service

import scala.concurrent.Future

trait JwtTokenExchangeService {
    def exchange(jwt: String): Future[Option[String]]
}