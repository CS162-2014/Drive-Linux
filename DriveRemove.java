/*Alec Snyder
 * cs162
 * Google Drive for Linux
 * Code MODIFIED FROM Google Quickstart drive example for Java
 * https://developers.google.com/drive/web/quickstart/quickstart-java
 * This Code adds a document to the user's Drive
 */
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
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
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.net.URL;


public class DriveRemove {

  private static String CLIENT_ID = "842617857460-1gm3qknepc16b9dei9brhgnkc12aqrds.apps.googleusercontent.com";
  private static String CLIENT_SECRET = "d7lWsLVBBOBQw4AkQxSE5sYH";
  private static String REFRESH_TOKEN;
  private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
  
  public static void remove(ArrayList<File> files) throws IOException {
    EasyReader reader=new EasyReader(System.getProperty("user.home")+"/gdrive/.drive_key");
    REFRESH_TOKEN = reader.readLine();
    reader.close();
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
    //get Access Token
    String urlStr = "https://accounts.google.com/o/oauth2/token";
    String param="client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&refresh_token="+REFRESH_TOKEN+"&grant_type=refresh_token";
    URL url=new URL(urlStr);
    HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
    con.setDoOutput(true);
    String code="";
    DataOutputStream stream=new DataOutputStream(con.getOutputStream());
    stream.writeBytes(param);
    stream.flush();
    stream.close();
    BufferedReader in =new BufferedReader(new InputStreamReader(con.getInputStream()));
    String input="";
    String res=in.readLine();
    res=in.readLine();
    String access=res.substring(20, res.length()-2);

    GoogleCredential credential = new GoogleCredential();
    // Set authorized credentials.
    credential.setAccessToken(access);
    
    //Create a new authorized API client
    Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build();

    //remove File by ID
    for(int i=0; i<files.size(); i++)
    {
        service.files().delete(files.get(i).getId()).execute();
    }
    //System.out.println("File ID: " + file.getId());
  }
  public static void removeFid(String fid) throws IOException
  {
    EasyReader reader=new EasyReader(System.getProperty("user.home")+"/gdrive/.drive_key");
    REFRESH_TOKEN = reader.readLine();
    reader.close();
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
    //get Access Token
    String urlStr = "https://accounts.google.com/o/oauth2/token";
    String param="client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET+"&refresh_token="+REFRESH_TOKEN+"&grant_type=refresh_token";
    URL url=new URL(urlStr);
    HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
    con.setDoOutput(true);
    String code="";
    DataOutputStream stream=new DataOutputStream(con.getOutputStream());
    stream.writeBytes(param);
    stream.flush();
    stream.close();
    BufferedReader in =new BufferedReader(new InputStreamReader(con.getInputStream()));
    String input="";
    String res=in.readLine();
    res=in.readLine();
    String access=res.substring(20, res.length()-2);

    GoogleCredential credential = new GoogleCredential();
    // Set authorized credentials.
    credential.setAccessToken(access);
    
    //Create a new authorized API client
    Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build();

    //remove File by ID
    service.files().delete(fid).execute();
    //System.out.println("File ID: " + file.getId());
  }
  public static void main(String[] args)
  {
      try
      {
          removeFid(args[0]);
      }
      catch(IOException e)
      {
          System.out.println("Error removing File by id: "+args[0]);
      }
  }
}
