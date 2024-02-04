package service.sheets

import scala.jdk.CollectionConverters._
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Sheet
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest
import com.google.api.client.googleapis.json.GoogleJsonResponseException

import java.io.IOException
import java.security.GeneralSecurityException
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest
import play.api.Logging
import com.google.api.services.sheets.v4.model._
import service.sheets.model.SheetException


object SheetsUtil extends ExecutionBatchGoogle {
    val APPLICATION_NAME: String = "Fuyuki-Gestion-Sheet"
    val baseURISheet: String = "https://docs.google.com/spreadsheets/d/"
    val sheetService: Sheets = DriveUtil.getSheetsService


    def addSheet(title: String, spreadsheetId: String) = {
        val requests = List(new Request().setAddSheet(new AddSheetRequest().setProperties(new SheetProperties().setTitle(title))))
        logger.info(s"Création de la sheet $title dans la spreadsheet $spreadsheetId")
        execute(sheetService.spreadsheets().batchUpdate(spreadsheetId, new BatchUpdateSpreadsheetRequest().setRequests(requests.asJava)))
    }

    def sendValuesToSheet(data: List[ValueRange], spreadsheetId: String): BatchUpdateValuesResponse = {
        val batchBody: BatchUpdateValuesRequest = new BatchUpdateValuesRequest().setValueInputOption("USER_ENTERED").setData(data.asJava);
        
        execute(sheetService.spreadsheets().values().batchUpdate(spreadsheetId, batchBody)) match {
            case Some(x) => x
            case None => throw new SheetException(s"SendValuesToSheet failed with data = $data\n et spreadsheet = $spreadsheetId")
        }
    }

    def getSheetsFromSpreadSheet(sheet: String, spreadsheetId: String, range: String): Sheet = {
        sheetService.spreadsheets().get(spreadsheetId).setRanges(List(range).asJava).setIncludeGridData(true).execute().getSheets().asScala.find(x => x.getProperties().getTitle() == sheet) match {
            case None => throw new SheetException("Sheet not found, please check your input.")
            case Some(value) => value
        }
    }

    def getValueFromCell(cell: String, sheet: String, spreadsheetId: String): String = {
        val range: String = s"${sheet}!$cell"
        getSheetsFromSpreadSheet(sheet, spreadsheetId, range).getData().get(0).getRowData().get(0) match {
                case x: RowData => x.getValues().get(0) match {
                    case c: CellData => c.getFormattedValue()
                    case _ => throw new SheetException("Column not found, please check your input")
                }
                case _ => throw new SheetException("Row not found, please check your input.")
        }
    }

    def getValueLine(line: Integer, sheet: String, spreadsheetId: String): List[String] = {
        val range: String = s"${sheet}!$line:$line"
        getSheetsFromSpreadSheet(sheet, spreadsheetId, range).getData().get(0).getRowData().get(0) match {
            case x: RowData => x.getValues().asScala.map(_.getFormattedValue()).toList
            case _ => throw new SheetException("Row not found, please check your input.")
        }
    }

    
    def getIdsSheet(spreadsheetId: String): List[String] = sheetService.spreadsheets().get(spreadsheetId).execute().getSheets().asScala.map(s => s.getProperties().getTitle()).toList

    def getValueColumn(column: String, sheet: String, spreadsheetId: String): List[String] = {
        val range: String = s"${sheet}!$column:$column"
        getSheetsFromSpreadSheet(sheet, spreadsheetId, range).getData().asScala.apply(0).getRowData() match {
                case l: java.util.List[RowData] => l.asScala.map(x => {
                    x.getValues() match {
                        case l: java.util.List[CellData] => l.get(0) match {
                            case c: CellData => Option[String](c.getFormattedValue()).getOrElse("")
                            case null => ""
                            }
                        case null => ""
                    }
                }).toList
                case null => List()
            }
    }
}