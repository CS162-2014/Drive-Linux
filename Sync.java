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
        while(!console.eof())
        {
            if(!File.isIn(files, home+line))
            {
                try
                {
                    String id=DriveInsert.up(home+line);
                    File newFile=new File();
                    newFile.name=home+line;
                    newFile.fid=id;
                    try
                    {
                        newFile.md5sum=File.getMdSum(newFile.name);
                    }
                    catch(IOException e)
                    {
                        System.out.println("error creating md5sum");
                        newFile.md5sum="temp";
                    }
                    files.add(newFile);
                }
                catch(IOException e)
                {
                    System.out.println("Error, unable to upload");
                }
            }
            line=console.readLine();
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
