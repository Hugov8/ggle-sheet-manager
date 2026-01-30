package service

import com.google.api.services.sheets.v4.model.Spreadsheet
import service.sheets.SpreadSheetUtil
import com.google.api.services.sheets.v4.model.ValueRange
import scala.jdk.CollectionConverters._
import service.sheets.SheetsUtil
import service.sheets.model.SpreadSheetLink
import play.api.Logger


class SheetService(val tokenUser: String) {
    val sheetUtil: SheetsUtil = SheetsUtil(tokenUser)

    def generateSpreadSheet(name: String, mail: String): Spreadsheet  = {
        val res = SpreadSheetUtil.createSpreadSheet(name, tokenUser)
        res
    }
    def writeCells(lienspreadSheet: SpreadSheetLink, startCell: String, sheetName: String, values: List[List[String]]): Unit = {
        val rangeScript: String = "'"+sheetName+"'!"+startCell
        val valuesRange : ValueRange = new ValueRange().setRange(rangeScript).setValues(values.map(_.map(_.asInstanceOf[Object]).asJava).asJava)
        sheetUtil.sendValuesToSheet(List(valuesRange), lienspreadSheet.getSpreadSheetId)
    }

    def addSheetToSpreadsheet(url: SpreadSheetLink, nameSheet: String) = sheetUtil.addSheet(nameSheet, url.getSpreadSheetId)

    def writeOneCell     (lienspreadSheet: SpreadSheetLink, numCell:   String, sheetName: String, value: String)       : Unit = writeCells(lienspreadSheet, numCell,   sheetName, List(List(value)))
    def writeCellsColumn (lienspreadSheet: SpreadSheetLink, startCell: String, sheetName: String, values: List[String]): Unit = writeCells(lienspreadSheet, startCell, sheetName, values.map(List(_)))
    def writeCellsLine   (lienspreadSheet: SpreadSheetLink, startCell: String, sheetName: String, values: List[String]): Unit = writeCells(lienspreadSheet, startCell, sheetName, List(values))

    def getValueCell(lienspreadSheet: SpreadSheetLink, nomSheet: String, numCell:   String): String = sheetUtil.getValueFromCell(numCell, nomSheet, lienspreadSheet.getSpreadSheetId)
    
    def getValueColumn(lienspreadSheet: SpreadSheetLink, nomSheet: String, column: String): List[String] = sheetUtil.getValueColumn(column, nomSheet, lienspreadSheet.getSpreadSheetId)
    def getValueLine(lienspreadSheet: SpreadSheetLink, nomSheet: String, line: Integer): List[String] = sheetUtil.getValueLine(line, nomSheet, lienspreadSheet.getSpreadSheetId)
    def getSheetId(lienSpreadsheet: SpreadSheetLink): List[String] = sheetUtil.getIdsSheet(lienSpreadsheet.getSpreadSheetId)

}

object SheetService {
    def apply(token: String): SheetService = new SheetService(token)
}
