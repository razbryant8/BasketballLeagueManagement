package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.*;
import databaseConnection.*;
import oracle.jdbc.*;

public class AddGameForm extends JFrame{
	private final int HEIGHT= 600, WIDTH = 600;
	private OracleConnectionDB orccdb;
	private JLabel roundLabel,homeTeamLabel,homeTeamScoreLabel, awayTeamLabel,awayTeamScoreLabel;
	private JPanel roundPanel,homeTeamPanel ,awayTeamPanel, buttonPanel;
	private JTextField homeScoreTF,awayScoreTF;
	private JButton addGameButton;
	private JComboBox<String> roundsCB,homeTeamCB, awayTeamCB;
	private JFrame this_frame;
	
	public AddGameForm(String name, OracleConnectionDB orccdb, String leagueName) {
		ResultSet res;
		this.setTitle(name);
		this.orccdb = orccdb;
		this.setLayout(new GridLayout(4,1));
		this_frame = this;
		
		roundPanel = new JPanel();
		roundPanel.setLayout(new FlowLayout());
		roundLabel = new JLabel("Choose Round Date : ");
		roundPanel.add(roundLabel);
		roundsCB = new JComboBox<String>();
		try {
			
			
			this.orccdb.setCallableStatement(this.orccdb.getConnection().prepareCall("{? = call league_pack.get_rounds_dates_in_league_function( ? )}"));
			this.orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
			this.orccdb.getCalStat().setString(2, leagueName);
			this.orccdb.getCalStat().execute();
			this.orccdb.setResultSet(((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1));
			
			res = ((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1);
			

			while(res.next()) {
				roundsCB.addItem(res.getString(1));
			}
			
			
		} catch (SQLException e) {e.printStackTrace();}
		roundPanel.add(roundsCB);
		this.add(roundPanel);
		
		homeTeamPanel = new JPanel();
		homeTeamPanel.setLayout(new FlowLayout());
		homeTeamLabel = new JLabel("Choose Home Team: ");
		homeTeamPanel.add(homeTeamLabel);
		homeTeamCB = new JComboBox<String>();
		try {
			
			
			this.orccdb.setCallableStatement(this.orccdb.getConnection().prepareCall("{? = call league_pack.get_teams_names_in_league_function( ? )}"));
			this.orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
			this.orccdb.getCalStat().setString(2, leagueName);
			this.orccdb.getCalStat().execute();
			this.orccdb.setResultSet(((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1));
			
			res = ((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1);
			

			while(res.next()) {
				homeTeamCB.addItem(res.getString(1));
			}
			
			
		} catch (SQLException e) {e.printStackTrace();}
		homeTeamPanel.add(homeTeamCB);
		homeTeamScoreLabel = new JLabel("Enter Home Team Score: ");
		homeTeamPanel.add(homeTeamScoreLabel);
		homeScoreTF = new JTextField(10);
		homeTeamPanel.add(homeScoreTF);
		this.add(homeTeamPanel);
		
		awayTeamPanel = new JPanel();
		awayTeamPanel.setLayout(new FlowLayout());
		awayTeamLabel = new JLabel("Choose Away Team: ");
		awayTeamPanel.add(awayTeamLabel);
		awayTeamCB = new JComboBox<String>();
		try {
			
			
			this.orccdb.setCallableStatement(this.orccdb.getConnection().prepareCall("{? = call league_pack.get_teams_names_in_league_function( ? )}"));
			this.orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
			this.orccdb.getCalStat().setString(2, leagueName);
			this.orccdb.getCalStat().execute();
			this.orccdb.setResultSet(((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1));
			
			res = ((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1);
			

			while(res.next()) {
				awayTeamCB.addItem(res.getString(1));
			}
			
			
		} catch (SQLException e) {e.printStackTrace();}
		awayTeamPanel.add(awayTeamCB);
		awayTeamScoreLabel = new JLabel("Enter Away Team Score: ");
		awayTeamPanel.add(awayTeamScoreLabel);
		awayScoreTF = new JTextField(10);
		awayTeamPanel.add(awayScoreTF);
		this.add(awayTeamPanel);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		addGameButton = new JButton("Add Game");
		addGameButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  try {
						
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{call league_pack.add_new_game( ? , ? , ? , ? , ? )}"));
						orccdb.getCalStat().setString(1, String.valueOf(roundsCB.getSelectedItem()).substring(0, 10));
						orccdb.getCalStat().setString(2, String.valueOf(homeTeamCB.getSelectedItem()));
						orccdb.getCalStat().setInt(3, Integer.valueOf(homeScoreTF.getText()));
						orccdb.getCalStat().setString(4, String.valueOf(awayTeamCB.getSelectedItem()));
						orccdb.getCalStat().setInt(5, Integer.valueOf(awayScoreTF.getText()));
						orccdb.getCalStat().execute();
						
						
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{call league_pack.UPDATE_WINS_AND_LOSSES_TO_ALL_TEAMS_IN_ALL_LEAGUES}"));
						orccdb.getCalStat().execute();
						
						JOptionPane.showMessageDialog(this_frame,"New game has been added.");
						
					} catch (SQLException e1) {
						if(e1.getErrorCode()==20001) JOptionPane.showMessageDialog(this_frame, "You have selected the same team to home and away", "Error", JOptionPane.ERROR_MESSAGE);
						else if(e1.getErrorCode()==20002) JOptionPane.showMessageDialog(this_frame, "The game cant ended in TIE!", "Error", JOptionPane.ERROR_MESSAGE);
						else JOptionPane.showMessageDialog(this_frame, "An error has occurred", "Error", JOptionPane.ERROR_MESSAGE);
					}
			  } 
			} );
		buttonPanel.add(addGameButton);
		
		
		this.add(buttonPanel);
		
		
		
		
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		this.setVisible(true);
		
	}
}
