/* Alec Snyder
 * cs162
 * Main Gui frontend for google drive
 * Creates a menu that calls all the other programs for the google drive program
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.Runtime;
import java.io.IOException;
import java.lang.Process;

public class DriveGui implements ActionListener
{
    public JFrame frame;
    private JButton sync, list, upload, quit, rem;
    
    public DriveGui()
    {
        frame=new JFrame("Welcome to Google Drive!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2,1));
        sync=new JButton("Sync");
        list=new JButton("Download Files from Drive");
        upload=new JButton("Upload File");
        quit=new JButton("Quit");
        rem=new JButton("Remove Files from Drive");
        JPanel bPanel=new JPanel();
        bPanel.setLayout(new GridLayout(1,5));
        sync.addActionListener(this);
        upload.addActionListener(this);
        list.addActionListener(this);
        rem.addActionListener(this);
        quit.addActionListener(this);
        bPanel.add(sync);
        bPanel.add(list);
        bPanel.add(upload);
        bPanel.add(rem);
        bPanel.add(quit);
        JLabel instr=new JLabel("What would you like to do?");
        frame.add(instr);
        frame.add(bPanel);
        frame.pack();
        frame.setVisible(true);
        File f=new File(System.getProperty("user.home")+"/gdrive/.drive_key");
        if(!f.exists())
        {
            new GuiRefresh();
        }
    }
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==(Object)(quit))
        {
            System.exit(0);
        }
        else if(e.getSource()==(Object)(upload))
        {
            JFileChooser fc=new JFileChooser();
            fc.setCurrentDirectory(new File(System.getProperty("user.home")));
            int val=fc.showOpenDialog(frame);
            if(val==JFileChooser.APPROVE_OPTION)
            {
                move(fc.getSelectedFile());
            }
        }
        else if(e.getSource()==(Object)(sync))
        {
            String[] args={};
            Sync.main(args);
        }
        else if(e.getSource()==(Object)(list))
        {
            new DownloadFrame();
        }
        else if(e.getSource()==(Object)(rem))
        {
            new RemoveFrame();
        }
    }
    
    public void move(File f)
    {
        try
        {
            DriveInsert.up(f.getAbsolutePath());
        }
        catch(IOException e)
        {
            System.out.println("error occurred");
        }
    }
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new DriveGui();
            }
        });
    }
}
