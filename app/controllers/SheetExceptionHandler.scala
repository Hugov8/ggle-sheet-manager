package controllers

import javax.inject.Singleton

import scala.concurrent._

import play.api.http.HttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._
import service.sheets.model.SheetException
import play.api.libs.json.Json

@Singleton
class SheetExceptionHandler extends HttpErrorHandler {
  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(
      Status(statusCode)(s"A client error occurred: $message with status $statusCode with request : $request")
    )
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = exception match {
        case e: SheetException => Future.successful(BadRequest(Json.obj("message"->e.getMessage())))
        case e: NumberFormatException => Future.successful(BadRequest(Json.obj("message"->s"Please check the line: ${e.getMessage()}")))
        case _: Throwable => Future.successful(InternalServerError("A server error occurred: " + exception.getMessage))
    }
}