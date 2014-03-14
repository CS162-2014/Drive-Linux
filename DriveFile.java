import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.lang.Runtime;
import java.lang.Process;
import java.io.BufferedReader;
import java.io.IOException;

public class DriveFile
{
    String name;
    String md5sum;
    String fid;
    
    public DriveFile()
    {
        name="";
        md5sum="";
        fid="";
    }
    
    public static DriveFile isIn(ArrayList<DriveFile> ls, String f)
    {
        for(int i=0; i<ls.size(); i++)
        {
            if(ls.get(i).name.equals(f))
            {
                return ls.get(i);
            }
        }
        return null;
    }
    
    public static String getMdSum(String f) throws IOException
    {
        Runtime r=Runtime.getRuntime();
        Process p=r.exec("md5sum "+f);
        InputStream in=p.getInputStream();
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        StringBuilder code=new StringBuilder();
        code.append(reader.readLine());
        return code.toString().substring(0,(code.toString().indexOf(' ')));
    }
        
}
