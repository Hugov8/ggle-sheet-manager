package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.http.MediaType
import play.api.libs.json.JsValue


case class Logging[A](action: Action[A]) extends Action[A] with play.api.Logging {
  def apply(request: Request[A]): Future[Result] = {
    logger.info(s"$request received from ${request.remoteAddress}")
    action(request)
  }

  override def parser           = action.parser
  override def executionContext = action.executionContext
}

class LoggingAction @Inject() (parser: BodyParsers.Default)(implicit ec: ExecutionContext)
    extends ActionBuilderImpl(parser) {
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    block(request)
  }
  override def composeAction[A](action: Action[A]): Logging[A] = new Logging(action)
}
