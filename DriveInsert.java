/*Alec Snyder
 * cs162
 * Google Drive for Linux
 * Code MODFIED FROM Google Quickstart drive example for Java
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
import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Path;


public class DriveInsert {

  private static String CLIENT_ID = "842617857460-1gm3qknepc16b9dei9brhgnkc12aqrds.apps.googleusercontent.com";
  private static String CLIENT_SECRET = "d7lWsLVBBOBQw4AkQxSE5sYH";
  private static String REFRESH_TOKEN;
  private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
  
  public static String up(String fname) throws IOException {
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

    //Insert a file  
    File body = new File();
    body.setTitle(getFileName(fname));
    body.setDescription("drive-linux upload");
    String mime=Files.probeContentType(FileSystems.getDefault().getPath(fname));
    body.setMimeType(mime);
    
    java.io.File fileContent = new java.io.File(fname);
    FileContent mediaContent = new FileContent(mime, fileContent);

    File file = service.files().insert(body, mediaContent).execute();
    return file.getId();
  }
  
  public static String getFileName(String f)
  {
      for(int i=f.length()-1; i>=0; i--)
      {
          if(f.charAt(i)=='/'&&f.charAt(i-1)!='\\')
          {
              return f.substring(i+1);
          }
      }
      return f;
  }
  public static void main(String[] args)
  {
      java.io.File ins=new java.io.File(args[0]);
      try{
        up(ins.getAbsolutePath());
    }
    catch(IOException e)
    {
        System.out.println("Error uploading file: "+args[0]);
    }
  }
}
