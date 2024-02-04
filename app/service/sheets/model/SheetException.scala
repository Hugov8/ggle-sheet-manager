package service.sheets.model

class SheetException(val message: String) extends Throwable {
    override def getMessage(): String = message
}
