/* Alec Snyder
 * cs162
 * Frame that allows the user to select which files they want to remove from Drive
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import com.google.api.services.drive.model.File;
import java.io.IOException;

public class RemoveFrame implements ActionListener
{
    public JFrame frame;
    public JPanel checkPanel, control;
    public JButton confirm, quit;
    public ArrayList<JCheckBox> boxes;
    public ArrayList<File> files;
    
    public RemoveFrame()
    {
        frame=new JFrame("Select Files you wish to Remove from Drive");
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
        confirm=new JButton("Remove");
        quit=new JButton("Cancel");
        confirm.addActionListener(this);
        quit.addActionListener(this);
        frame.setLayout(new BorderLayout());
        checkPanel=new JPanel();
        control=new JPanel();
        control.setLayout(new GridLayout(1,2));
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
        checkPanel.removeAll();
        checkPanel.setLayout(new GridLayout(files.size(), 1));
        for(int i=0; i<files.size(); i++)
        {
            JCheckBox box=new JCheckBox(files.get(i).getTitle());
            box.setActionCommand(files.get(i).getId());
            boxes.add(box);
            checkPanel.add(box);
        }
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==(Object)(quit))
        {
            frame.dispose();
        }
        else if(e.getSource()==(Object)(confirm))
        {
            ArrayList<File> rFiles=new ArrayList<File>();
            for(int i=0; i<boxes.size(); i++)
            {
                if(boxes.get(i).isSelected())
                {
                    rFiles.add(getFile(boxes.get(i).getActionCommand()));
                }
            }
            try
            {
                DriveRemove.remove(rFiles);
            }
            catch(IOException ex)
            {
                System.out.println("error removing");
            }
            frame.dispose();
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
        new RemoveFrame();
    }
}
