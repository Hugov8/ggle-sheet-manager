package service.sheets


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import scala.io.BufferedSource
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.auth.http.HttpCredentialsAdapter
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.services.drive.DriveScopes

object GoogleAuthorizeUtil {
    def authorizedToken(accessToken: String): Credential = {
        return new Credential(BearerToken.authorizationHeaderAccessMethod()).setAccessToken(accessToken)
    }
}