/* Alec Snyder
 * cs162
 * Class that syncs all in the ~/gdrive folder with drive
 * revision system so that if you change a file, it gets updated
 * metadata in XML format stored under ~/gdrive/gdrive.xml */
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
public class Sync
{
    public static ArrayList<DriveFile> populateFiles()
    {
        ArrayList<DriveFile> files=new ArrayList<DriveFile>();
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
            DriveFile f=new DriveFile();
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
        ArrayList<DriveFile> files=Sync.populateFiles();
        String home=System.getProperty("user.home")+"/gdrive/";
        ArrayList<String> ls=new ArrayList<String>();
        File driveDir=new File(System.getProperty("user.home")+"/gdrive");
        File[] fs=driveDir.listFiles();
        for(int i=0; i<fs.length; i++)
        {
            if(!fs[i].isHidden())
                ls.add(fs[i].getAbsolutePath());
        }
        for(int i=0; i<ls.size(); i++)
        {
            if(DriveFile.isIn(files, ls.get(i))==null)
            {
                try
                {
                    String id=DriveInsert.up(ls.get(i));
                    DriveFile newFile=new DriveFile();
                    newFile.name=ls.get(i);
                    newFile.fid=id;
                    try
                    {
                        newFile.md5sum=DriveFile.getMdSum(newFile.name);
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
                    DriveFile match=DriveFile.isIn(files,ls.get(i));
                    String sum=DriveFile.getMdSum(ls.get(i));
                    if(!sum.equals(match.md5sum))
                    {
                        DriveRemove.removeFid(match.fid);
                        match.fid=DriveInsert.up(ls.get(i));
                        match.md5sum=sum;
                    }
                }
                catch(IOException e)
                {
                    System.out.println("Unable to resolve md5 update for "+ls.get(i));
                }
            }
        }
        for(int i=0; i<files.size(); i++)
        {
            if(!ls.contains(files.get(i).name))
            {
                try
                {
                    DriveRemove.removeFid(files.get(i).fid);
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
