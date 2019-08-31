package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;

import databaseConnection.*;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;


public class AddTeamForm extends JFrame{
	private final int HEIGHT= 600, WIDTH = 600;
	private OracleConnectionDB orccdb;
	private JLabel teamNameLabel,arenaNameLabel;
	private JTextField teamNameTF;
	private JPanel formPanel;
	private JButton insertButton;
	private JComboBox<String> arenasNameCB;
	private JFrame this_frame;
	
	public AddTeamForm(String name, OracleConnectionDB orccdb) {
		ResultSet res;
		this.setTitle(name);
		this.orccdb = orccdb;
		this.setLayout(new BorderLayout());
		this_frame = this;
		
		formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(2,2));
		
		
		teamNameLabel = new JLabel("Team Name: ");
		formPanel.add(teamNameLabel);
		
		teamNameTF = new JTextField();
		formPanel.add(teamNameTF);
		
		arenaNameLabel = new JLabel("Arena Name: ");
		formPanel.add(arenaNameLabel);
		
		arenasNameCB = new JComboBox<String>();
		
		try {
			
			
			this.orccdb.setCallableStatement(this.orccdb.getConnection().prepareCall("{? = call league_pack.get_arenas_names_function}"));
			this.orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
			this.orccdb.getCalStat().execute();
			this.orccdb.setResultSet(((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1));
			
			res = ((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1);
			

			while(res.next()) {
				arenasNameCB.addItem(res.getString(1));
			}
			
			
		} catch (SQLException e) {e.printStackTrace();}
		
		
		formPanel.add(arenasNameCB);
		
		
		
		
		this.add(formPanel, BorderLayout.CENTER);
		
		insertButton = new JButton("Add Team");
		insertButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  try {
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{call league_pack.add_new_team( ? , ? )}"));
						orccdb.getCalStat().setString(1, teamNameTF.getText());
						orccdb.getCalStat().setString(2, String.valueOf(arenasNameCB.getSelectedItem()));
						orccdb.getCalStat().execute();
						JOptionPane.showMessageDialog(this_frame,"New team has been added.");
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
