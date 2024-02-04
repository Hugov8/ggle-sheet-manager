package service.sheets.model
import io.lemonlabs.uri._

class SpreadSheetLink(val lien: String) {
    def getLien: String = lien
    def getSpreadSheetId: String = AbsoluteUrl.parse(lien).path.parts(2)
    
}

object SpreadSheetLink {
    implicit def string2SpreadSheetLink(s: String): SpreadSheetLink = new SpreadSheetLink(s)
}