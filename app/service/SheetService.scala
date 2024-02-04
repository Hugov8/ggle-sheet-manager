package service

import com.google.api.services.sheets.v4.model.Spreadsheet
import service.sheets.SpreadSheetUtil
import com.google.api.services.sheets.v4.model.ValueRange
import scala.jdk.CollectionConverters._
import service.sheets.SheetsUtil
import service.sheets.model.SpreadSheetLink
import play.api.Logger


object SheetService {
    def generateSpreadSheet(name: String, mail: String): Spreadsheet  = {
        val res = SpreadSheetUtil.createSpreadSheet(name)
        shareSpreadsheet(res.getSpreadsheetUrl(), mail)
        res
    }
    def shareSpreadsheet(lien: SpreadSheetLink, mail: String): Unit = SpreadSheetUtil.shareSpreadsheet(lien.getSpreadSheetId, mail)
    def writeCells(lienspreadSheet: SpreadSheetLink, startCell: String, sheetName: String, values: List[List[String]]): Unit = {
        val rangeScript: String = "'"+sheetName+"'!"+startCell
        val valuesRange : ValueRange = new ValueRange().setRange(rangeScript).setValues(values.map(_.map(_.asInstanceOf[Object]).asJava).asJava)
        SheetsUtil.sendValuesToSheet(List(valuesRange), lienspreadSheet.getSpreadSheetId)
    }

    def addSheetToSpreadsheet(url: SpreadSheetLink, nameSheet: String) = SheetsUtil.addSheet(nameSheet, url.getSpreadSheetId)

    def writeOneCell     (lienspreadSheet: SpreadSheetLink, numCell:   String, sheetName: String, value: String)       : Unit = writeCells(lienspreadSheet, numCell,   sheetName, List(List(value)))
    def writeCellsColumn (lienspreadSheet: SpreadSheetLink, startCell: String, sheetName: String, values: List[String]): Unit = writeCells(lienspreadSheet, startCell, sheetName, values.map(List(_)))
    def writeCellsLine   (lienspreadSheet: SpreadSheetLink, startCell: String, sheetName: String, values: List[String]): Unit = writeCells(lienspreadSheet, startCell, sheetName, List(values))

    def getValueCell(lienspreadSheet: SpreadSheetLink, nomSheet: String, numCell:   String): String = SheetsUtil.getValueFromCell(numCell, nomSheet, lienspreadSheet.getSpreadSheetId)
    
    def getValueColumn(lienspreadSheet: SpreadSheetLink, nomSheet: String, column: String): List[String] = SheetsUtil.getValueColumn(column, nomSheet, lienspreadSheet.getSpreadSheetId)
    def getValueLine(lienspreadSheet: SpreadSheetLink, nomSheet: String, line: Integer): List[String] = SheetsUtil.getValueLine(line, nomSheet, lienspreadSheet.getSpreadSheetId)
    def getSheetId(lienSpreadsheet: SpreadSheetLink): List[String] = SheetsUtil.getIdsSheet(lienSpreadsheet.getSpreadSheetId)

}
