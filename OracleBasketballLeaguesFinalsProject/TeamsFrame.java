package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.table.*;

import databaseConnection.OracleConnectionDB;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;


public class TeamsFrame extends JFrame{
	private final int HEIGHT= 600, WIDTH = 600;
	private OracleConnectionDB orccdb;
	private JTable teamsTable;
	private JPanel tablePanel,addTeamPanel;
	private JButton addTeamButton, orderByTeamNameButton, orderByArenaNameButton;
	private JComboBox<String> orderByCB;
	private JFrame this_frame;
	private ResultSet res;
	
	
	public TeamsFrame(String name, OracleConnectionDB orccdb) {
		this.setTitle(name);
		this.orccdb = orccdb;
		this_frame = this;
		
		this.setLayout(new BorderLayout());
		
		tablePanel = new JPanel(new BorderLayout());
		
		teamsTable = new JTable(new DefaultTableModel(null,new Object[]{"Team Name","Arena Name"}));
		DefaultTableModel model = (DefaultTableModel) teamsTable.getModel();
		
		try {
			
			
			this.orccdb.setCallableStatement(this.orccdb.getConnection().prepareCall("{? = call league_pack.teams_refcursor_function( ? , ? )}"));
			this.orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
			this.orccdb.getCalStat().setString(2, "desc");
			this.orccdb.getCalStat().setString(3, "TEAM_NAME");
			this.orccdb.getCalStat().execute();
			this.orccdb.setResultSet(((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1));
			
			res = ((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1);

			
			while(res.next()) {
				model.addRow(new Object[] {res.getString(2), res.getString(3)});
			}
			
			
		} catch (SQLException e) {e.printStackTrace();}
		

		teamsTable.setEnabled(false);
		tablePanel.add(teamsTable.getTableHeader(),BorderLayout.PAGE_START);
		tablePanel.add(new JScrollPane(teamsTable));
		tablePanel.add(teamsTable,BorderLayout.CENTER);
		
		this.add(tablePanel, BorderLayout.CENTER);
		
		
		addTeamPanel = new JPanel();
		addTeamPanel.setLayout(new FlowLayout());
		
		addTeamButton = new JButton("Add Team");
		addTeamButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				new AddTeamForm("Add Team Form", orccdb);
				this_frame.dispose();
			  } 
			} );
		addTeamPanel.add(addTeamButton);
		
		
		orderByTeamNameButton = new JButton("Order By Team Name");
		orderByTeamNameButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  DefaultTableModel model = (DefaultTableModel) teamsTable.getModel();
				  int rowCount = model.getRowCount();
				  //Remove rows one by one from the end of the table
				  for (int i = rowCount - 1; i >= 0; i--) {
					  model.removeRow(i);
				  }
				  
				  DefaultTableModel model1 = (DefaultTableModel) teamsTable.getModel();
					
					try {
						
						
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{? = call league_pack.teams_refcursor_function( ? , ? )}"));
						orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
						orccdb.getCalStat().setString(2, String.valueOf(orderByCB.getSelectedItem()));
						orccdb.getCalStat().setString(3, "TEAM_NAME");
						orccdb.getCalStat().execute();
						orccdb.setResultSet(((OracleCallableStatement)orccdb.getCalStat()).getCursor(1));
						
						res = ((OracleCallableStatement)orccdb.getCalStat()).getCursor(1);

						
						while(res.next()) {
							model1.addRow(new Object[] {res.getString(2), res.getString(3)});
						}
						
						
					} catch (SQLException e1) {e1.printStackTrace();}
				  
				  
				  
			  } 
			} );
		addTeamPanel.add(orderByTeamNameButton);
		
		orderByArenaNameButton = new JButton("Order By Arena Name");
		orderByArenaNameButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  DefaultTableModel model = (DefaultTableModel) teamsTable.getModel();
				  int rowCount = model.getRowCount();
				  //Remove rows one by one from the end of the table
				  for (int i = rowCount - 1; i >= 0; i--) {
					  model.removeRow(i);
				  }
				  
				  DefaultTableModel model1 = (DefaultTableModel) teamsTable.getModel();
					
					try {
						
						
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{? = call league_pack.teams_refcursor_function( ? , ? )}"));
						orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
						orccdb.getCalStat().setString(2, String.valueOf(orderByCB.getSelectedItem()));
						orccdb.getCalStat().setString(3, "ARENA_NAME");
						orccdb.getCalStat().execute();
						orccdb.setResultSet(((OracleCallableStatement)orccdb.getCalStat()).getCursor(1));
						
						res = ((OracleCallableStatement)orccdb.getCalStat()).getCursor(1);

						
						while(res.next()) {
							model1.addRow(new Object[] {res.getString(2), res.getString(3)});
						}
						
						
					} catch (SQLException e1) {e1.printStackTrace();}
				  
				 
				  
			  } 
			} );
		addTeamPanel.add(orderByArenaNameButton);
		
		orderByCB = new JComboBox<String>(new String[] {"desc","asc"});
		addTeamPanel.add(orderByCB);
		
		this.add(addTeamPanel, BorderLayout.PAGE_END);
		
		
		
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		this.setVisible(true);
	}

}
