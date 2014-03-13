/* Alec Snyder
 * cs162
 * Google Drive for linux
 * grabs a refresh token and saves it to .drive metafile
 * code MODIFIED FROM Google drive Quickstart code: https://developers.google.com/drive/web/quickstart/quickstart-java
 */
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.auth.oauth2.TokenResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class DriveRefreshGen {

  private static String CLIENT_ID = "842617857460-1gm3qknepc16b9dei9brhgnkc12aqrds.apps.googleusercontent.com";
  private static String CLIENT_SECRET = "d7lWsLVBBOBQw4AkQxSE5sYH";

  private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
  
  public static String getURL() //separate helper method accessible outside the class to get the url
  {
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
   
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
        .setAccessType("offline")
        .setApprovalPrompt("force").build();
    //give authorization URL
    String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    return url;
    }
    
    public static void genRefresh(String authcode) throws IOException
    {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
   
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
            .setAccessType("offline")
            .setApprovalPrompt("force").build();
        GoogleTokenResponse response = flow.newTokenRequest(authcode).setRedirectUri(REDIRECT_URI).execute();
        GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(new JacksonFactory()).setTransport(new NetHttpTransport()).setClientSecrets(CLIENT_ID, CLIENT_SECRET).build();
        credential.setFromTokenResponse(response);
        //save refresh token for later
        EasyWriter writer=new EasyWriter(System.getProperty("user.home")+"/gdrive/.drive_key");
        writer.println(credential.getRefreshToken());
        writer.close();
    }
      
  public static void main(String[] args) throws IOException {
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
   
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
        .setAccessType("offline")
        .setApprovalPrompt("force").build();
    //give authorization URL
    String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    System.out.println("Please open the following URL in your browser then type the authorization code:");
    System.out.println("  " + url);
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String code = br.readLine();
    //grab code, build credential and parse refresh token
    GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
    GoogleCredential credential = new GoogleCredential.Builder().setJsonFactory(new JacksonFactory()).setTransport(new NetHttpTransport()).setClientSecrets(CLIENT_ID, CLIENT_SECRET).build();
    credential.setFromTokenResponse(response);
    //save refresh token for later
    EasyWriter writer=new EasyWriter(System.getProperty("user.home")+"/gdrive/.drive_key");
    writer.println(credential.getRefreshToken());
    writer.close();
  }
}
