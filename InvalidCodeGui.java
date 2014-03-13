/* Alec Snyder
 * cs162
 * A small helper class to display if the user inputs the wrong code for the oauth2 two step verification
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InvalidCodeGui implements ActionListener
{
    public JFrame frame;
    public JButton confirm;
    
    public InvalidCodeGui()
    {
        frame=new JFrame("Invalid Code");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        confirm=new JButton("ok");
        frame.getRootPane().setDefaultButton(confirm);
        frame.setLayout(new GridLayout(2,1));
        JLabel label=new JLabel("Your Code was invalid, please try again!");
        confirm.addActionListener(this);
        frame.add(label);
        frame.add(confirm);
        frame.pack();
        frame.setSize(new Dimension(500,500));
        frame.setVisible(true);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        frame.dispose();
    }
}
