package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

import databaseConnection.*;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

public class AddPlayerForm extends JFrame{
	private final int HEIGHT= 600, WIDTH = 600;
	private OracleConnectionDB orccdb;
	private JLabel firstNameLabel,lastNameLabel, teamNameLabel, joinDateLabel, jerseyNumberLabel, salaryLabel, positionLabel;
	private JTextField firstNameTF,lastNameTF, joinDateTF, jerseyNumberTF, salaryTF;
	private JComboBox<String> teamNameCB, positionCB;
	private JPanel formPanel;
	private JButton insertButton;
	private JFrame this_frame;
	
	public AddPlayerForm(String name, OracleConnectionDB orccdb) {
		ResultSet res;
		this.setTitle(name);
		this.orccdb = orccdb;
		this.setLayout(new BorderLayout());
		this_frame = this;
		
		formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(7,2));
		
		
		firstNameLabel = new JLabel("First Name: ");
		formPanel.add(firstNameLabel);
		
		firstNameTF = new JTextField();
		formPanel.add(firstNameTF);
		
		lastNameLabel = new JLabel("Last Name: ");
		formPanel.add(lastNameLabel);
		
		lastNameTF = new JTextField();
		formPanel.add(lastNameTF);
		
		teamNameLabel = new JLabel("Team Name: ");
		formPanel.add(teamNameLabel);
		
		teamNameCB = new JComboBox<String>();
		try {
			
			
			this.orccdb.setCallableStatement(this.orccdb.getConnection().prepareCall("{? = call league_pack.get_teams_names_function}"));
			this.orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
			this.orccdb.getCalStat().execute();
			this.orccdb.setResultSet(((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1));
			
			res = ((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1);
			

			while(res.next()) {
				teamNameCB.addItem(res.getString(1));
			}
			
			
		} catch (SQLException e) {e.printStackTrace();}
		
		formPanel.add(teamNameCB);
		
		
		joinDateLabel = new JLabel("Join Date: ");
		formPanel.add(joinDateLabel);
		
		joinDateTF = new JTextField();
		formPanel.add(joinDateTF);
		
		jerseyNumberLabel = new JLabel("Jersey Number: ");
		formPanel.add(jerseyNumberLabel);
		
		jerseyNumberTF = new JTextField();
		formPanel.add(jerseyNumberTF);
		
		salaryLabel = new JLabel("Salary: ");
		formPanel.add(salaryLabel);
		
		salaryTF = new JTextField();
		formPanel.add(salaryTF);
		
		positionLabel= new JLabel("Position: ");
		formPanel.add(positionLabel);
		
		positionCB = new JComboBox<String>(new String[] {"PG","SG","SF","PF","C"});
		formPanel.add(positionCB);
		
		
		this.add(formPanel, BorderLayout.CENTER);
		
		insertButton = new JButton("Add Player");
		insertButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  try {
						
						
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{call league_pack.add_new_player( ? , ? , ? , ? , ? , ? , ?)}"));
						orccdb.getCalStat().setString(1, firstNameTF.getText());
						orccdb.getCalStat().setString(2, lastNameTF.getText());
						orccdb.getCalStat().setString(3, String.valueOf(teamNameCB.getSelectedItem()));
						orccdb.getCalStat().setString(4, joinDateTF.getText());
						orccdb.getCalStat().setInt(5, Integer.valueOf(jerseyNumberTF.getText()));
						orccdb.getCalStat().setInt(6, Integer.valueOf(salaryTF.getText()));
						orccdb.getCalStat().setString(7, String.valueOf(positionCB.getSelectedItem()));
						orccdb.getCalStat().execute();
						
						JOptionPane.showMessageDialog(this_frame,"New player has been added.");
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
