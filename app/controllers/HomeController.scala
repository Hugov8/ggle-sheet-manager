package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import service.SheetService
import model.ModificationSheetInputCell
import play.api.libs.json.Reads
import play.api.libs.json.JsPath
import play.api.libs.json.JsError
import play.api.libs.json.Json
import model.ModificationSheetInputColumnOrLine
import model.ModificationSheetInputValues
import model.ShareSpreadsheetInput
import model.GenerateSpreadsheetInput
import com.google.api.services.sheets.v4.model.Spreadsheet
import model.AddSheetInput
import service.sheets.SheetsUtil

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class SheetController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def share() = Action(parse.json) {
    request => request.body.validate[ShareSpreadsheetInput].fold(
      error => BadRequest(Json.obj("message" -> JsError.toJson(error))),
      input => {
        SheetService.shareSpreadsheet(input.lienSpreadsheet, input.mail)
        Ok(Json.obj("state"->"Success"))
      }
    )
  }

  def generate() = Action(parse.json) {
    request => request.body.validate[GenerateSpreadsheetInput].fold(
      error => BadRequest(Json.obj("message" -> JsError.toJson(error))),
      input => {
        val res: Spreadsheet = SheetService.generateSpreadSheet(input.name, input.mail)
        Ok(Json.obj("state"->"success", "result"->res.getSpreadsheetUrl()))
      }
    )
  }

  def addSheet() = Action(parse.json) {
    request => request.body.validate[AddSheetInput].fold(
      error => BadRequest(Json.obj("message"->JsError.toJson(error))),
      input => {
          SheetService.addSheetToSpreadsheet(input.lienSpreadsheet, input.nameSheet)
          Ok(Json.obj("state"->"success"))
      }
    )
  }

  def getSheetIds() = Action {
    request => request.headers.get("lienSpreadsheet") match {
      case None => BadRequest(Json.obj("message"->"lienSpreadsheet is missing in headers"))
      case Some(value) => Ok(Json.obj("response"->SheetService.getSheetId(value)))
    } 
  }

  def getValueCell(sheetName: String, cell: String) = Action {
    request => request.headers.get("lienSpreadsheet") match {
      case None => BadRequest(Json.obj("message"->"lienSpreadsheet is missing in headers"))
      case Some(lien) => {
        val res = SheetService.getValueCell(lien, sheetName, cell)
        Ok(Json.obj("state"->"success", "response"->res))
      }
    }
  }

  def getValueCellsLine(sheetName: String, line: Integer) = Action {
    request => request.headers.get("lienSpreadsheet") match {
      case None => BadRequest(Json.obj("message"->"lienSpreadsheet is missing in headers"))
      case Some(lien) => {
        val res = SheetService.getValueLine(lien, sheetName, line.toInt)
        Ok(Json.obj("state"->"success", "response"->res))
      }
    }
  }

  def getValueCellsColumn(sheetName: String, column: String) = Action {
    request => request.headers.get("lienSpreadsheet") match {
      case None => BadRequest(Json.obj("message"->"lienSpreadsheet is missing in headers"))
      case Some(lien) => {
        val res = SheetService.getValueColumn(lien, sheetName, column)
        Ok(Json.obj("state"->"success", "response"->res))
      }
    }
  }

  def writeCell() = Action(parse.json) { 
    request => request.body.validate[ModificationSheetInputCell].fold(
        error => BadRequest(Json.obj("message"-> JsError.toJson(error))),
        input => {
          SheetService.writeOneCell(input.lienSpreadsheet, input.cell, input.nameSheet, input.value)
          Ok(Json.obj("state"-> "success"))
        })
  }

  def writeCellsColumn() = Action(parse.json) { 
    request => request.body.validate[ModificationSheetInputColumnOrLine].fold(
        error => BadRequest(Json.obj("message"-> JsError.toJson(error))),
        input => {
          SheetService.writeCellsColumn(input.lienSpreadsheet, input.cell, input.nameSheet, input.values)
          Ok(Json.obj("message"-> "Success"))
        })
  }

  def writeCellsLine() = Action(parse.json) { 
    request => request.body.validate[ModificationSheetInputColumnOrLine].fold(
        error => BadRequest(Json.obj("message"-> JsError.toJson(error))),
        input => {
          SheetService.writeCellsLine(input.lienSpreadsheet, input.cell, input.nameSheet, input.values)
          Ok(Json.obj("message"-> "Success"))
        })
  }

  def writeCells() = Action(parse.json) { 
    request => request.body.validate[ModificationSheetInputValues].fold(
        error => BadRequest(Json.obj("message"-> JsError.toJson(error))),
        input => {
          SheetService.writeCells(input.lienSpreadsheet, input.cell, input.nameSheet, input.values)
          Ok(Json.obj("message"-> "Success"))
        })
  }

}
