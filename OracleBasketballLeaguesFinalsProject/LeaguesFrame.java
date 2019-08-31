package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import databaseConnection.OracleConnectionDB;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

public class LeaguesFrame extends JFrame{
	private final int HEIGHT= 600, WIDTH = 600;
	private JComboBox<String> leagueNameCB, teamsNamesCB;
	private JButton leagueChoiseButton, showGamesListButton, addTeamToCurrentLeagueButton , numOfTeamsButton;
	private JPanel leagueChoosePanel, leaguePanel, lowerPanel , teamToLeaguePanel;
	private OracleConnectionDB orccdb;
	private JTable leagueTable;
	private JFrame this_frame;
	
	public LeaguesFrame(String name, OracleConnectionDB orccdb) {
		this.setTitle(name);
		this.orccdb = orccdb;
		this.setLayout(new BorderLayout());
		ResultSet res;
		this_frame = this;
		
		
		leagueChoosePanel = new JPanel(new FlowLayout());
		
		leagueNameCB = new JComboBox<String>();
		
		try {
			this.orccdb.setCallableStatement(this.orccdb.getConnection().prepareCall("{call league_pack.UPDATE_WINS_AND_LOSSES_TO_ALL_TEAMS_IN_ALL_LEAGUES}"));
			this.orccdb.getCalStat().execute();
			
			this.orccdb.setCallableStatement(this.orccdb.getConnection().prepareCall("{? = call league_pack.get_leagues_names_function}"));
			this.orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
			this.orccdb.getCalStat().execute();
			this.orccdb.setResultSet(((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1));
			
			res = ((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1);
			

			while(res.next()) {
				leagueNameCB.addItem(res.getString(1));
			}
			
			
		} catch (SQLException e) {e.printStackTrace();}
		
		
		leagueChoosePanel.add(leagueNameCB);
		
		leagueChoiseButton = new JButton("Show");
		leagueChoiseButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  DefaultTableModel model = (DefaultTableModel) leagueTable.getModel();
				  int rowCount = model.getRowCount();
				  //Remove rows one by one from the end of the table
				  for (int i = rowCount - 1; i >= 0; i--) {
					  model.removeRow(i);
				  }
				  
					try {
						ResultSet res1;
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{? = call league_pack.get_league_table_function( ? )}"));
						orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
						orccdb.getCalStat().setString(2, String.valueOf(leagueNameCB.getSelectedItem()));
						orccdb.getCalStat().execute();
						orccdb.setResultSet(((OracleCallableStatement)orccdb.getCalStat()).getCursor(1));
						
						res1 = ((OracleCallableStatement)orccdb.getCalStat()).getCursor(1);
						
						
						while(res1.next()) {
							model.addRow(new Object[] {res1.getString(1), res1.getInt(2), res1.getInt(3)});
						}
						
						
					} catch (Exception e1) {e1.printStackTrace();}
					addTeamToCurrentLeagueButton.setEnabled(true);
			  } 
			} );
		leagueChoosePanel.add(leagueChoiseButton);
		
		numOfTeamsButton = new JButton("Number of teams in league");
		numOfTeamsButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  int num = -1;
					try {
						
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{? = call league_pack.get_number_of_teams_in_league( ? )}"));
						orccdb.getCalStat().registerOutParameter(1, OracleTypes.NUMBER);
						orccdb.getCalStat().setString(2, String.valueOf(leagueNameCB.getSelectedItem()));
						orccdb.getCalStat().execute();
						num = orccdb.getCalStat().getInt(1);
						JOptionPane.showMessageDialog(this_frame,"The number of teams in "+ String.valueOf(leagueNameCB.getSelectedItem()) +" is "+num, "Message", JOptionPane.INFORMATION_MESSAGE);
						
					} catch (Exception e1) {e1.printStackTrace();}
			  } 
			} );
		leagueChoosePanel.add(numOfTeamsButton);
		
		this.add(leagueChoosePanel, BorderLayout.PAGE_START);
		
		leaguePanel = new JPanel();
			
		leagueTable = new JTable(new DefaultTableModel(null,new Object[]{"Team Name","Number Of Wins","Number Of Losses"}));
		

		leagueTable.setEnabled(false);
		leaguePanel.add(leagueTable.getTableHeader(),BorderLayout.PAGE_START);
		leaguePanel.add(new JScrollPane(leagueTable));
		
		this.add(leaguePanel, BorderLayout.CENTER);
		
		lowerPanel = new JPanel();
		lowerPanel.setLayout(new GridLayout(2, 1));
		
		teamToLeaguePanel = new JPanel();
		teamToLeaguePanel.setLayout(new FlowLayout());
		
		
		teamsNamesCB = new JComboBox<String>();
		try {
			
			
			this.orccdb.setCallableStatement(this.orccdb.getConnection().prepareCall("{? = call league_pack.get_teams_names_function}"));
			this.orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
			this.orccdb.getCalStat().execute();
			this.orccdb.setResultSet(((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1));
			
			res = ((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1);
			

			while(res.next()) {
				teamsNamesCB.addItem(res.getString(1));
			}
			
			
		} catch (SQLException e) {e.printStackTrace();}
		teamToLeaguePanel.add(teamsNamesCB);
		
		addTeamToCurrentLeagueButton = new JButton("Add Team To Current Shown League");
		addTeamToCurrentLeagueButton.setEnabled(false);
		addTeamToCurrentLeagueButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  
			  try {	
					orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{call league_pack.link_team_to_league( ? , ? )}"));
					orccdb.getCalStat().setString(1, String.valueOf(teamsNamesCB.getSelectedItem()));
					orccdb.getCalStat().setString(2, String.valueOf(leagueNameCB.getSelectedItem()));
					orccdb.getCalStat().execute();

					DefaultTableModel model = (DefaultTableModel) leagueTable.getModel();
					  int rowCount = model.getRowCount();
					  //Remove rows one by one from the end of the table
					  for (int i = rowCount - 1; i >= 0; i--) {
						  model.removeRow(i);
					  }
				} catch (SQLException e1) {e1.printStackTrace();}  
			  try {
				  DefaultTableModel model1 = (DefaultTableModel) leagueTable.getModel();

					ResultSet res1;
					orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{? = call league_pack.get_league_table_function( ? )}"));
					orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
					orccdb.getCalStat().setString(2, String.valueOf(leagueNameCB.getSelectedItem()));
					orccdb.getCalStat().execute();
					orccdb.setResultSet(((OracleCallableStatement)orccdb.getCalStat()).getCursor(1));
					
					res1 = ((OracleCallableStatement)orccdb.getCalStat()).getCursor(1);
					
					
					while(res1.next()) {
						model1.addRow(new Object[] {res1.getString(1), res1.getInt(2), res1.getInt(3)});
					}
					
					
				} catch (Exception e1) {e1.printStackTrace();}
			  } 
			} );
		teamToLeaguePanel.add(addTeamToCurrentLeagueButton);
		lowerPanel.add(teamToLeaguePanel);
		
		showGamesListButton = new JButton("Show Games List");
		showGamesListButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				new GamesInLeagueFrame("Game List of "+String.valueOf(leagueNameCB.getSelectedItem()), orccdb,String.valueOf(leagueNameCB.getSelectedItem()));
			  } 
			} );
		lowerPanel.add(showGamesListButton);
		
		this.add(lowerPanel, BorderLayout.PAGE_END);
		
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

}
