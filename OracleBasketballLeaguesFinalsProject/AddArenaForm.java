package gui;

import java.awt.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import databaseConnection.OracleConnectionDB;

public class AddArenaForm extends JFrame{
	private final int HEIGHT= 600, WIDTH = 600;
	private OracleConnectionDB orccdb;
	private JLabel arenaNameLabel,arenaCityLabel,numberOfSeatsLabel;
	private JTextField arenaNameTF,arenaCityTF, numberOfSeatsTF;
	private JPanel formPanel;
	private JButton insertButton;
	private JFrame this_frame;
	
	public AddArenaForm(String name, OracleConnectionDB orccdb) {
		ResultSet res;
		this.setTitle(name);
		this.orccdb = orccdb;
		this.setLayout(new BorderLayout());
		this_frame = this;
		
		formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(3,2));
		
		
		arenaNameLabel = new JLabel("Arena Name: ");
		formPanel.add(arenaNameLabel);
		
		arenaNameTF = new JTextField();
		formPanel.add(arenaNameTF);
		
		arenaCityLabel = new JLabel("City: ");
		formPanel.add(arenaCityLabel);
		
		arenaCityTF = new JTextField();
		formPanel.add(arenaCityTF);
		
		numberOfSeatsLabel = new JLabel("Number Of Seats: ");
		formPanel.add(numberOfSeatsLabel);
		
		numberOfSeatsTF = new JTextField();
		formPanel.add(numberOfSeatsTF);
		
		
		
		this.add(formPanel, BorderLayout.CENTER);
		
		insertButton = new JButton("Add Arena");
		insertButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  try {
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{call league_pack.add_new_arena( ? , ? , ? )}"));
						orccdb.getCalStat().setString(1, arenaNameTF.getText());
						orccdb.getCalStat().setString(2, arenaCityTF.getText());
						orccdb.getCalStat().setInt(3, Integer.valueOf(numberOfSeatsTF.getText()));
						orccdb.getCalStat().execute();
						JOptionPane.showMessageDialog(this_frame,"New arena has been added.");
					} catch (SQLException e1) {e1.printStackTrace();}
			  } 
			} );
		this.add(insertButton, BorderLayout.PAGE_END);
		
		
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		this.setVisible(true);
		
	}
}
