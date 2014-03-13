/* Alec Snyder
 * cs162
 * Class that syncs all in the ~/gdrive folder with drive
 * revision system so that if you change a file, it gets updated
 * metadata in XML format stored under ~/gdrive/gdrive.xml */
import java.util.ArrayList;
import java.io.IOException;
public class Sync
{
    public static ArrayList<File> populateFiles()
    {
        ArrayList<File> files=new ArrayList<File>();
        String home=System.getProperty("user.home")+"/gdrive/";
        EasyReader reader=new EasyReader(home+".drive.xml");
        String line=reader.readLine();
        reader.readLine();
        line=reader.readLine();
        while(!line.equals("</files>"))
        {
            line=reader.readLine();
            String name=line.substring(8,line.length()-7);
            line=reader.readLine();
            String id=line.substring(6,line.length()-5);
            line=reader.readLine();
            String mdsum=line.substring(9,line.length()-8);
            File f=new File();
            f.name=name;
            f.fid=id;
            f.md5sum=mdsum;
            files.add(f);
            reader.readLine();
            line=reader.readLine();
        }
        reader.close();
        return files;
    }
    
    public static void main(String[] args)
    {
        ArrayList<File> files=Sync.populateFiles();
        String home=System.getProperty("user.home")+"/gdrive/";
        EasyReader console=new EasyReader();
        String line=console.readLine();
        ArrayList<String> ls=new ArrayList<String>();
        while(!console.eof())
        {
            ls.add(line);
            line=console.readLine();
        }
        for(int i=0; i<ls.size(); i++)
        {
            if(File.isIn(files, home+ls.get(i))==null)
            {
                try
                {
                    String id=DriveInsert.up(home+ls.get(i));
                    File newFile=new File();
                    newFile.name=home+ls.get(i);
                    newFile.fid=id;
                    try
                    {
                        newFile.md5sum=File.getMdSum(newFile.name);
                    }
                    catch(IOException e)
                    {
                        System.out.println("error creating md5sum for "+ls.get(i));
                        newFile.md5sum="temp";
                    }
                    files.add(newFile);
                }
                catch(IOException e)
                {
                    System.out.println("Error, unable to upload "+ls.get(i));
                }
            }
            else
            {
                try
                {
                    File match=File.isIn(files, home+ls.get(i));
                    String sum=File.getMdSum(home+ls.get(i));
                    if(!sum.equals(match.md5sum))
                    {
                        DriveRemove.remove(match.fid);
                        match.fid=DriveInsert.up(home+ls.get(i));
                        match.md5sum=sum;
                    }
                }
                catch(IOException e)
                {
                    System.out.println("Unable to resolve md5 update for "+home+ls.get(i));
                }
            }
        }
        for(int i=0; i<files.size(); i++)
        {
            if(!ls.contains(DriveInsert.getFileName(files.get(i).name)))
            {
                try
                {
                    DriveRemove.remove(files.get(i).fid);
                    files.remove(i);
                }
                catch(IOException e)
                {
                    System.out.println("Error deleting file: "+files.get(i).name);
                }
            }
        }
        EasyWriter writer=new EasyWriter(home+".drive.xml");
        writer.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        writer.print("<files>\n");
        for(int i=0; i<files.size(); i++)
        {
            writer.print("\t<file>\n");
            writer.print("\t\t<name>"+files.get(i).name+"</name>\n");
            writer.print("\t\t<id>"+files.get(i).fid+"</id>\n");
            writer.print("\t\t<mdsum>"+files.get(i).md5sum+"</mdsum>\n");
            writer.print("\t</file>\n");
        }
        writer.print("</files>");
        writer.close();
    }
}
