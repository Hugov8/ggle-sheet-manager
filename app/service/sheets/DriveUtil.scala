package service.sheets


import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest
import com.google.api.client.googleapis.json.GoogleJsonResponseException

import com.google.api.client.googleapis.batch.BatchRequest;


import java.io.IOException;
import java.security.GeneralSecurityException;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest
import com.google.api.services.drive.model.Permission;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback
import com.google.api.client.http.HttpHeaders
import com.google.api.client.googleapis.json.GoogleJsonError
import play.api.Logger
import com.google.api.services.sheets.v4.Sheets
import play.api.Logging
import model.SheetException


object DriveUtil {
    val logger: Logger = Logger(getClass())
    val APPLICATION_NAME: String = "Fuyuki-Generation-Sheet"

    def getDriveService: Drive = {
        val credential = GoogleAuthorizeUtil.authorize
        return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build()
    }
    
    def getSheetsService: Sheets  = {
        val credential = GoogleAuthorizeUtil.authorize
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).setApplicationName(APPLICATION_NAME).build()
    }
}

trait ExecutionBatchGoogle extends Logging {
    
    def execute[T](batch: AbstractGoogleClientRequest[T]): Option[T] = {
        try{
            Some(batch.execute())
        } catch {
            case e: GoogleJsonResponseException => {
                e.getStatusCode() match {
                    case 400 => throw new SheetException(e.getDetails().getMessage())
                    case 429 => logger.info("Sommeil pendant 60s");Thread.sleep(60_000);execute(batch)
                    case _ => throw new SheetException(e.getDetails().getMessage())
                }
            }
            case e: Throwable => logger.debug(e.getMessage());throw e;
        }
    }
}