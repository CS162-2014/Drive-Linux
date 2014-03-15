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
import com.google.api.services.drive.model.FileList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;

public class DriveList {

  private static String CLIENT_ID = "842617857460-1gm3qknepc16b9dei9brhgnkc12aqrds.apps.googleusercontent.com";
  private static String CLIENT_SECRET = "d7lWsLVBBOBQw4AkQxSE5sYH";
  private static String REFRESH_TOKEN;
  private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
  
  public static ArrayList<File> list() throws IOException {
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

    //put files in ArrayList
    ArrayList<File> result=new ArrayList<File>();
    Drive.Files.List request =service.files().list();
    do
    {
        FileList files= request.execute();
        result.addAll(files.getItems());
        request.setPageToken(files.getNextPageToken());
    } while(request.getPageToken() != null && request.getPageToken().length()>0);
    
    return result; //return ArrayList
  }
  public static void main(String[] args)
  {
      //Access Array List and print out
      try {
      ArrayList<File> result=DriveList.list(); 
      System.out.println("______________________");
      for(int i=0; i<result.size(); i++)
      {
          System.out.println(result.get(i).getTitle()+", ID=["+result.get(i).getId()+"]");
      }
      }
      catch (IOException e)
      {
          System.out.println("Error getting list!");
      }
  }
}
