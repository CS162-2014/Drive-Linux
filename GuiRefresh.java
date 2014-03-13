/* Alec Snyder
 * cs162
 * Gui Program to get a new refresh token
 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
public class GuiRefresh implements ActionListener
{
    public JFrame frame;
    private JButton confirm, cancel;
    private JTextField field;
    
    public GuiRefresh()
    {
        frame=new JFrame("Generate Refresh Token");
        frame.setLayout(new GridLayout(5,1));
        JLabel urlInstr=new JLabel("Please navigate to the following URL in a web browser:");
        JTextArea url=new JTextArea();
        url.setEditable(false);
        url.setText(DriveRefreshGen.getURL());
        JLabel codeInstr=new JLabel("Please copy the code that you see into the box below then press enter");
        field=new JTextField(100);
        confirm=new JButton("confirm");
        cancel=new JButton("cancel");
        cancel.addActionListener(this);
        confirm.addActionListener(this);
        frame.getRootPane().setDefaultButton(confirm);
        JPanel bPanel=new JPanel();
        bPanel.setLayout(new GridLayout(1,2));
        bPanel.add(confirm);
        bPanel.add(cancel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(urlInstr);
        frame.add(url);
        frame.add(codeInstr);
        frame.add(field);
        frame.add(bPanel);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==(Object)(cancel))
        {
            System.exit(0);
        }
        else if(e.getSource()==(Object)(confirm))
        {
            try
            {
                DriveRefreshGen.genRefresh(field.getText());
                frame.dispose();
            }
            catch(IOException ex)
            {
                field.setText("");
                new InvalidCodeGui();
            }
        }
    }
    
    public static void main(String[] args)
    {
        new GuiRefresh();
    }
}
    
