/*Alec Snyder
 * cs162
 * Google Drive for Linux
 * Code MODIFIED FROM Google Quickstart drive example for Java
 * https://developers.google.com/drive/web/quickstart/quickstart-java
 * This code Downloads a given document from the drive given the file ID number
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
import com.google.api.client.http.HttpResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.net.URL;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import com.google.api.client.http.GenericUrl;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class DriveDownload {

  private static String CLIENT_ID = "842617857460-1gm3qknepc16b9dei9brhgnkc12aqrds.apps.googleusercontent.com";
  private static String CLIENT_SECRET = "d7lWsLVBBOBQw4AkQxSE5sYH";
  private static String REFRESH_TOKEN;
  private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
  
  public static void downloadFiles(ArrayList<File> files) throws IOException
  {
        EasyReader reader=new EasyReader(System.getProperty("user.home")+"/gdrive/.drive_key");
        REFRESH_TOKEN = reader.readLine();
        reader.close();
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
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
        credential.setAccessToken(access);
        Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
        String home=System.getProperty("user.home")+"/gdrive/";
        ArrayList<DriveFile> fs=Sync.populateFiles();
        for(int i=0; i<files.size(); i++)
        {
            DriveFile match=DriveFile.isIn(fs, home+files.get(i).getTitle());
            if(match!=null)
            {
                HttpResponse response = service.getRequestFactory().buildGetRequest(new GenericUrl(files.get(i).getDownloadUrl())).execute();
                InputStream downStream=response.getContent();
                //write content of downloaded file to file on local storage
                Files.copy(downStream, Paths.get(System.getProperty("user.home")+"/gdrive/"+files.get(i).getTitle()), StandardCopyOption.REPLACE_EXISTING);
                match.fid=files.get(i).getId();
                try
                {
                    match.md5sum=DriveFile.getMdSum(match.name);
                }
                catch(IOException e)
                {
                    System.out.println("error getting md5sum");
                }
            }
            else
            {
                if(files.get(i).getDownloadUrl()==null)
                    continue;
                HttpResponse response = service.getRequestFactory().buildGetRequest(new GenericUrl(files.get(i).getDownloadUrl())).execute();
                InputStream downStream=response.getContent();
                //write content of downloaded file to file on local storage
                Files.copy(downStream, Paths.get(System.getProperty("user.home")+"/gdrive/"+files.get(i).getTitle()), StandardCopyOption.REPLACE_EXISTING);
                DriveFile newFile=new DriveFile();
                newFile.name=home+files.get(i).getTitle();
                newFile.fid=files.get(i).getId();
                try
                {
                    newFile.md5sum=DriveFile.getMdSum(home+files.get(i).getTitle());
                }
                catch(IOException e)
                {
                    System.out.println("error getting md5sum");
                }
                fs.add(newFile);
            }
        }
        EasyWriter writer=new EasyWriter(home+".drive.xml");
        writer.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        writer.print("<files>\n");
        for(int i=0; i<fs.size(); i++)
        {
            writer.print("\t<file>\n");
            writer.print("\t\t<name>"+fs.get(i).name+"</name>\n");
            writer.print("\t\t<id>"+fs.get(i).fid+"</id>\n");
            writer.print("\t\t<mdsum>"+fs.get(i).md5sum+"</mdsum>\n");
            writer.print("\t</file>\n");
        }
        writer.print("</files>");
        writer.close();
    }
  
  public static void main(String[] args) {
    String id=args[0];
    ArrayList<File> result;
    File down=new File();
    try
    {
        result=DriveList.list();
        for(int i=0; i<result.size(); i++)
        {
            if(result.get(i).getId().equals(id))
            {
                down=result.get(i);
            }
        }
    }
    catch(IOException e)
    {
        System.out.println("Error in listing drive contents");
    }
    try {
    /* Create Service */
    EasyReader reader=new EasyReader(System.getProperty("user.home")+"/gdrive/.drive_key");
    REFRESH_TOKEN = reader.readLine();
    reader.close();
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
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
    HttpResponse response = service.getRequestFactory().buildGetRequest(new GenericUrl(down.getDownloadUrl())).execute();
    InputStream downStream=response.getContent();
    //write content of downloaded file to file on local storage
    Files.copy(downStream, Paths.get(System.getProperty("user.home")+"/gdrive/"+down.getTitle()), StandardCopyOption.REPLACE_EXISTING);
    }
    catch(IOException e)
    {
        System.out.println("error Downloading file by ID: "+args[0]);
    }

  }
}
