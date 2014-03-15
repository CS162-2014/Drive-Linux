/* Alec Snyder
 * cs162
 * Pulls all the files from gdrive
 * overwrites any local changes!
 */
import java.util.ArrayList;
import com.google.api.services.drive.model.File;
import java.io.IOException;
public class DrivePull
{
    public static void main(String[] args)
    {
        try {
            ArrayList<File> files=DriveList.list();
            DriveDownload.downloadFiles(files);
        }
        catch(IOException e)
        {
            System.out.println("Error pulling all files from drive");
        }
    }
}
