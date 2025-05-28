import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SleepInterface extends JFrame implements ActionListener{ // FOR EXPERIMENTATION
		private JFrame window;
		private JPanel panel;
	    private JLabel label;
	    private JTextField textField;
	    private JButton button;

	    public SleepInterface() {
	    	window = new JFrame();
	    	panel = new JPanel();
	    	
	    	window.setTitle("Sleep Tracker");
	    	window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    	window.setSize(800, 500);
	    	window.setLocationRelativeTo(null);
	    	window.setVisible(true);
	    	
	        setTitle("Sleep Data Viewer Interface");
	        setSize(300, 200);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLayout(new FlowLayout());

	        label = new JLabel("Enter Text:");
	        textField = new JTextField(15);
	        button = new JButton("Submit");
	        button.addActionListener(this);

	        add(label);
	        add(textField);
	        add(button);

	        setVisible(true);
	    }

	    @Override
	    public void actionPerformed(ActionEvent e) {
	        if (e.getSource() == button) {
	            String text = textField.getText();
	            JOptionPane.showMessageDialog(this, "You entered: " + text);
	        }
	    }

	    public static void main(String[] args) {
	        new SleepInterface();
	    }

}
