package gui;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import databaseConnection.OracleConnectionDB;

public class LoginFrame extends JFrame {
	private final int HEIGHT= 600, WIDTH = 600;
	private JPanel headlinePanel,loginPanel,detailsPanel;
	private JLabel welcomeLabel,usernameLabel, passwordLabel,imageLabel,blankLabel;
	private JTextField usernameTF;
	private JPasswordField passwordTF;
	private JButton loginButton;
	private OracleConnectionDB orccdb;
	private JFrame this_frame;
	
	
	public LoginFrame(String name, OracleConnectionDB orccdb) {
		this_frame = this;
		this.setTitle(name);
		this.orccdb = orccdb;
		
		headlinePanel = new JPanel();
		headlinePanel.setLayout(new GridLayout(3,1));
		
		loginPanel = new JPanel();
		loginPanel.setLayout(new BorderLayout());

		welcomeLabel = new JLabel("Welcome to basketball leagues");
		welcomeLabel.setHorizontalAlignment(JLabel.CENTER);
		welcomeLabel.setVerticalAlignment(JLabel.CENTER);
		welcomeLabel.setFont(new Font("Ariel",Font.BOLD,30));
		headlinePanel.add(welcomeLabel,BorderLayout.PAGE_START);
		
	    ImageIcon imageIcon = new ImageIcon("basketballPhoto.jpg");
		imageLabel = new JLabel(imageIcon);
		headlinePanel.add(imageLabel,BorderLayout.CENTER);
		
		detailsPanel = new JPanel(new GridLayout(3, 2));
		loginPanel.add(detailsPanel, BorderLayout.CENTER);
		
		usernameLabel = new JLabel("Username: ");
		usernameLabel.setFont(new Font("Ariel",Font.BOLD,20));
		usernameLabel.setHorizontalAlignment(JLabel.CENTER);
		usernameLabel.setVerticalAlignment(JLabel.CENTER);
		detailsPanel.add(usernameLabel);
		
		usernameTF = new JTextField(20);
		detailsPanel.add(usernameTF);
		
		passwordLabel = new JLabel("Password: ");
		passwordLabel.setFont(new Font("Ariel",Font.BOLD,20));
		passwordLabel.setHorizontalAlignment(JLabel.CENTER);
		passwordLabel.setVerticalAlignment(JLabel.CENTER);
		detailsPanel.add(passwordLabel);
		
		passwordTF = new JPasswordField(20);
		detailsPanel.add(passwordTF);
		
		blankLabel = new JLabel("");
		detailsPanel.add(blankLabel);

		
		loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				try {
					orccdb.connectToDB(usernameTF.getText(), String.copyValueOf(passwordTF.getPassword()));
					try {
						new MenuFrame("Menu", orccdb);
						this_frame.dispose();
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(this_frame, "Wrong Username or Password", "Error in login", JOptionPane.ERROR_MESSAGE);
				}
			  } 
			} );
		detailsPanel.add(loginButton);
		
		headlinePanel.add(loginPanel, BorderLayout.PAGE_END);
		this.add(headlinePanel);
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	
	
}
