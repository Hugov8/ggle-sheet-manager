package model

import play.api.libs.json.Format
import play.api.libs.json.Json


sealed trait ModificationSheetInputCommon {
    def lienSpreadsheet: String 
    def nameSheet: String
    def cell: String
}

case class ModificationSheetInputColumnOrLine(override val lienSpreadsheet: String, override val nameSheet: String, override val cell: String, val values: List[String]) extends ModificationSheetInputCommon
case class ModificationSheetInputCell(override val lienSpreadsheet: String, override val nameSheet: String, override val cell: String, val value: String) extends ModificationSheetInputCommon
case class ModificationSheetInputValues(override val lienSpreadsheet: String, override val nameSheet: String, override val cell: String, val values: List[List[String]]) extends ModificationSheetInputCommon

case class ShareSpreadsheetInput(val lienSpreadsheet: String, val mail: String)
case class GenerateSpreadsheetInput(val name: String, val mail: String)
case class AddSheetInput(val lienSpreadsheet: String, val nameSheet: String)

object ModificationSheetInputCell {
    implicit val fmt: Format[ModificationSheetInputCell] = Json.format[ModificationSheetInputCell]
}

object ModificationSheetInputColumnOrLine {
    implicit val fmt: Format[ModificationSheetInputColumnOrLine] = Json.format[ModificationSheetInputColumnOrLine]
}

object ModificationSheetInputValues {
    implicit val fmt: Format[ModificationSheetInputValues] = Json.format[ModificationSheetInputValues]
}

object ShareSpreadsheetInput {
    implicit val fmt: Format[ShareSpreadsheetInput] = Json.format[ShareSpreadsheetInput]
}

object GenerateSpreadsheetInput {
    implicit val fmt: Format[GenerateSpreadsheetInput] = Json.format[GenerateSpreadsheetInput]
}

object AddSheetInput {
    implicit val fmt: Format[AddSheetInput] = Json.format[AddSheetInput]
}