package service.sheets

import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import play.api.Logging
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.Permission
import com.google.api.client.googleapis.batch.BatchRequest

import com.google.api.client.googleapis.batch.json.JsonBatchCallback
import com.google.api.client.http.HttpHeaders
import com.google.api.client.googleapis.json.GoogleJsonError
import model.SheetException

object SpreadSheetUtil extends ExecutionBatchGoogle with Logging {
    val sheetService = DriveUtil.getSheetsService
    def createSpreadSheet(nameSpreadsheet: String): Spreadsheet = {
        val spreadSheet = new Spreadsheet().setProperties(new SpreadsheetProperties().setTitle(nameSpreadsheet))
        execute(sheetService.spreadsheets().create(spreadSheet)) match {
            case Some(x) => logger.info(s"Spreadsheet créé $x");x
            case None => throw new SheetException(s"Création échoué pour la spreadsheet = $nameSpreadsheet")
        }
    }

    def shareSpreadsheet(idSpreadSheet: String, mail: String) = {
        val service: Drive = DriveUtil.getDriveService
        val permission: Permission = new Permission().setType("user").setRole("writer").setEmailAddress(mail)
        val batch: BatchRequest = service.batch()
        val callback = new JsonBatchCallback[Permission]() {
            def onFailure(e: GoogleJsonError, responseHeaders: HttpHeaders) = {
                throw new SheetException(s"Le partage n'a pas abouti $e")
            }
            def onSuccess(permission: Permission, responseHeaders: HttpHeaders) = {
                logger.info(s"Permission ID:  ${permission.getId()} to $mail");
            }
        }

        service.permissions().create(idSpreadSheet, permission).setFields("id").queue(batch, callback)
        batch.execute()
    }
}