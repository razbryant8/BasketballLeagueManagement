package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import databaseConnection.*;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

public class GamesInLeagueFrame extends JFrame{
	private final int HEIGHT= 600, WIDTH = 800;
	private OracleConnectionDB orccdb;
	private JTable gamesTable;
	private JPanel tablePanel, buttonsPanel, roundPanel;
	private JButton addRoundButton, addGameButton;
	private JTextField roundDateTF;
	private JLabel roundDateLabel;
	private JFrame this_frame;
	
	public GamesInLeagueFrame(String name, OracleConnectionDB orccdb, String leagueName) {
		ResultSet res;
		this.setTitle(name);
		this.orccdb = orccdb;
		this.setLayout(new BorderLayout());
		this_frame = this;
		
		tablePanel = new JPanel(new BorderLayout());
		
		gamesTable = new JTable(new DefaultTableModel(null,new Object[]{"Round Date","Home Team","Away Team","Home Score","Away Score"}));
		DefaultTableModel model = (DefaultTableModel) gamesTable.getModel();
		
		try {
			
			
			orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{? = call league_pack.get_game_list_function( ? )}"));
			orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
			orccdb.getCalStat().setString(2,leagueName);
			orccdb.getCalStat().execute();
			orccdb.setResultSet(((OracleCallableStatement)orccdb.getCalStat()).getCursor(1));
			
			res = ((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1);
			
			while(res.next()) {
				model.addRow(new Object[] {res.getString(1), res.getString(2), res.getString(3), 
						 res.getInt(4), res.getInt(5)});
			}
			
			
		} catch (SQLException e) {e.printStackTrace();}
		

		gamesTable.setEnabled(false);
		tablePanel.add(gamesTable.getTableHeader(),BorderLayout.PAGE_START);
		tablePanel.add(new JScrollPane(gamesTable));
		tablePanel.add(gamesTable,BorderLayout.CENTER);
		
		this.add(tablePanel, BorderLayout.CENTER);
		
		buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new GridLayout(2,1));
		
		roundPanel = new JPanel();
		roundPanel.setLayout(new FlowLayout());
		
		roundDateLabel = new JLabel("Enter new round date : ");
		roundPanel.add(roundDateLabel);
		
		roundDateTF = new JTextField(20);
		roundPanel.add(roundDateTF);
		
		addRoundButton = new JButton("Add Round");
		addRoundButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  try {
						
						
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{call league_pack.add_new_round( ? , ? )}"));
						orccdb.getCalStat().setString(1, leagueName);
						orccdb.getCalStat().setString(2, roundDateTF.getText());
						orccdb.getCalStat().execute();
						JOptionPane.showMessageDialog(this_frame,"New round has been added.");
					} catch (SQLException e1) {e1.printStackTrace();}
			  } 
			} );
		roundPanel.add(addRoundButton);
		
		buttonsPanel.add(roundPanel);
		
		addGameButton = new JButton("Add Game");
		addGameButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  new AddGameForm("Add Game Form to "+leagueName, orccdb, leagueName);
				  this_frame.dispose();
			  } 
			} );
		buttonsPanel.add(addGameButton);
		
		this.add(buttonsPanel, BorderLayout.PAGE_END);
		
		
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		this.setVisible(true);
	}

}
