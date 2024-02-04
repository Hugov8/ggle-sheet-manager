package service.sheets


import com.google.api.client.auth.oauth2.Credential;
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
    def authorizeUserAccount: Credential = {
        val in: InputStream = getClass().getClassLoader().getResourceAsStream("ressources/client_secret_oauth.json")
        val clientSecrets: GoogleClientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));

        val scopes: List[String] = Arrays.asList(SheetsScopes.SPREADSHEETS);

        val flow: GoogleAuthorizationCodeFlow = new GoogleAuthorizationCodeFlow.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clientSecrets, scopes).setDataStoreFactory(new MemoryDataStoreFactory())
                .setAccessType("offline").build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    def authorizeServiceAccount: HttpRequestInitializer = {
        val in: InputStream = getClass().getClassLoader().getResourceAsStream("ressources/client_secret_service.json")

        val scopes: List[String] = Arrays.asList(SheetsScopes.SPREADSHEETS, DriveScopes.DRIVE_FILE);
        val credentials = GoogleCredentials.fromStream(in).createScoped(scopes);
        return new HttpCredentialsAdapter(credentials);
    }

    val authorize = authorizeServiceAccount

}