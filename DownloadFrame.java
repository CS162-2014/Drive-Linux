/* Alec Snyder
 * cs162
 * Frame that allow user to select files from the drive they want to download
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import com.google.api.services.drive.model.File;
import java.io.IOException;

public class DownloadFrame implements ActionListener
{
    public JFrame frame;
    public JPanel checkPanel, control;
    public JCheckBox include, all;
    public JButton confirm, quit;
    public ArrayList<JCheckBox> boxes;
    public ArrayList<File> files;
    public ArrayList<DriveFile> fs=Sync.populateFiles();
    public final String home=System.getProperty("user.home")+"/gdrive/";
    
    public DownloadFrame()
    {
        frame=new JFrame("Select Files you wish to download");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getRootPane().setDefaultButton(confirm);
        try
        {
            files=DriveList.list();
        }
        catch(IOException e)
        {
            files=new ArrayList<File>();
        }
        confirm=new JButton("Download");
        quit=new JButton("Cancel");
        confirm.addActionListener(this);
        quit.addActionListener(this);
        frame.setLayout(new BorderLayout());
        checkPanel=new JPanel();
        include=new JCheckBox("Include only Files not synced");
        include.setSelected(true);
        all=new JCheckBox("Select All");
        include.addActionListener(this);
        all.addActionListener(this);
        control=new JPanel();
        control.setLayout(new GridLayout(2,2));
        control.add(include);
        control.add(all);
        control.add(confirm);
        control.add(quit);
        frame.add(control, BorderLayout.SOUTH);
        drawCheckPanel();
        frame.add(checkPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void drawCheckPanel()
    {
        boxes=new ArrayList<JCheckBox>();
        if(include.isSelected())
        {
            for(int i=0; i<files.size(); i++)
            {
                if(DriveFile.isIn(fs, home+files.get(i).getTitle())==null)
                {
                    JCheckBox box=new JCheckBox(files.get(i).getTitle());
                    box.setActionCommand(files.get(i).getId());
                    boxes.add(box);
                }
            }
        }
        else
        {
            for(int i=0; i<files.size(); i++)
            {
                JCheckBox box=new JCheckBox(files.get(i).getTitle());
                box.setActionCommand(files.get(i).getId());
                boxes.add(box);
            }
        }
        checkPanel.removeAll();
        checkPanel.setLayout(new GridLayout(boxes.size(), 1));
        for(int i=0; i<boxes.size(); i++)
        {
            if(all.isSelected())
            {
                boxes.get(i).setSelected(true);
            }
            checkPanel.add(boxes.get(i));
        }
        frame.pack();
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==(Object)(quit))
        {
            frame.dispose();
        }
        else if(e.getSource()==(Object)(confirm))
        {
            ArrayList<File> dFiles=new ArrayList<File>();
            for(int i=0; i<boxes.size(); i++)
            {
                if(boxes.get(i).isSelected())
                {
                    dFiles.add(getFile(boxes.get(i).getActionCommand()));
                }
            }
            try
            {
                DriveDownload.downloadFiles(dFiles);
            }
            catch(IOException ex)
            {
                System.out.println("error downloading");
            }
            frame.dispose();
        }
        else if(e.getSource()==(Object)(all))
        {
            if(all.isSelected())
            {
                for(int i=0; i<boxes.size(); i++)
                {
                    boxes.get(i).setSelected(true);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        }
        else if(e.getSource()==(Object)(include))
        {
            drawCheckPanel();
            frame.revalidate();
            frame.repaint();
        }
    }
    
    public File getFile(String fid)
    {
        for(int i=0; i<files.size(); i++)
        {
            if(files.get(i).getId().equals(fid))
            {
                return files.get(i);
            }
        }
        return null;
    }
    
    public static void main(String[] args)
    {
        new DownloadFrame();
    }
}
