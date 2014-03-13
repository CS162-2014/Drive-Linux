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
    private JButton sync, list, upload, quit;
    
    public DriveGui()
    {
        frame=new JFrame("Welcome to Google Drive!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(2,1));
        sync=new JButton("Sync");
        list=new JButton("List Files in Drive");
        upload=new JButton("Upload File");
        quit=new JButton("Quit");
        JPanel bPanel=new JPanel();
        bPanel.setLayout(new GridLayout(1,4));
        sync.addActionListener(this);
        list.addActionListener(this);
        upload.addActionListener(this);
        quit.addActionListener(this);
        bPanel.add(sync);
        bPanel.add(list);
        bPanel.add(upload);
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
            try
            {
                Process p=Runtime.getRuntime().exec("./gdrive");
                p.waitFor();
            }
            catch(IOException ex)
            {
                System.out.println("Sync command not called");
            }
            catch(InterruptedException ex)
            {
                System.out.println("Warning! Sync not finished!");
            }
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
