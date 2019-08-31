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

public class PlayersFrame extends JFrame{
	private final int HEIGHT= 600, WIDTH = 800;
	private OracleConnectionDB orccdb;
	private JTable playersTable;
	private JPanel tablePanel,addPlayerPanel,playersChoosePanel;
	private JButton addPlayerButton, showPositionButton, sumSalButton, upgradeSalButton;
	private JFrame this_frame;
	private JRadioButton allCHB, pgCHB, sgCHB, sfCHB, pfCHB, cCHB;
	private ButtonGroup group;
	private ResultSet res;
	
	
	public PlayersFrame(String name, OracleConnectionDB orccdb) {
		this.setTitle(name);
		this.orccdb = orccdb;
		this.setLayout(new BorderLayout());
		this_frame = this;

		
		
		tablePanel = new JPanel(new BorderLayout());
		
		playersTable = new JTable(new DefaultTableModel(null,new Object[]{"First Name","Last Name","Team Name","Join Date","Jersey Number","Salary","Position"}));
		DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
		
		try {
			
			
			this.orccdb.setCallableStatement(this.orccdb.getConnection().prepareCall("{? = call league_pack.players_refcursor_function( ? )}"));
			this.orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
			this.orccdb.getCalStat().setString(2, "All");
			this.orccdb.getCalStat().execute();
			this.orccdb.setResultSet(((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1));
			
			res = ((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1);
			
			while(res.next()) {
				model.addRow(new Object[] {res.getString(2), res.getString(3), res.getString(4), 
						res.getString(5), res.getInt(6), res.getInt(7), res.getString(8)});
			}
			
			
		} catch (SQLException e) {e.printStackTrace();}
		

		playersTable.setEnabled(false);
		tablePanel.add(playersTable.getTableHeader(),BorderLayout.PAGE_START);
		tablePanel.add(new JScrollPane(playersTable));
		tablePanel.add(playersTable,BorderLayout.CENTER);
		
		this.add(tablePanel, BorderLayout.CENTER);
		
		addPlayerPanel = new JPanel();
		addPlayerPanel.setLayout(new GridLayout(2, 1));
		
		
		playersChoosePanel = new JPanel();
		playersChoosePanel.setLayout(new FlowLayout());
		
		group = new ButtonGroup();	
		
		allCHB = new JRadioButton("All");
		group.add(allCHB);
		allCHB.setSelected(true);
		playersChoosePanel.add(allCHB);
		
		pgCHB = new JRadioButton("PG");
		group.add(pgCHB);
		playersChoosePanel.add(pgCHB);

		sgCHB = new JRadioButton("SG");
		group.add(sgCHB);
		playersChoosePanel.add(sgCHB);
		
		sfCHB = new JRadioButton("SF");
		group.add(sfCHB);
		playersChoosePanel.add(sfCHB);
		
		pfCHB = new JRadioButton("PF");
		group.add(pfCHB);
		playersChoosePanel.add(pfCHB);
		
		cCHB = new JRadioButton("C");
		group.add(cCHB);
		playersChoosePanel.add(cCHB);
		
		showPositionButton = new JButton("Show");
		showPositionButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
				  int rowCount = model.getRowCount();
				  //Remove rows one by one from the end of the table
				  for (int i = rowCount - 1; i >= 0; i--) {
					  model.removeRow(i);
				  }
				  
				  DefaultTableModel model1 = (DefaultTableModel) playersTable.getModel();
					
					try {
						
						
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{? = call league_pack.players_refcursor_function( ? )}"));
						orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
						orccdb.getCalStat().setString(2, getTextFromRB());
						orccdb.getCalStat().execute();
						orccdb.setResultSet(((OracleCallableStatement)orccdb.getCalStat()).getCursor(1));
						
						res = ((OracleCallableStatement)orccdb.getCalStat()).getCursor(1);
						
						while(res.next()) {
							model1.addRow(new Object[] {res.getString(2), res.getString(3), res.getString(4), 
									res.getString(5), res.getInt(6), res.getInt(7), res.getString(8)});
						}
						
						
					} catch (SQLException e1) {e1.printStackTrace();}
				  
				  
			  } 
			} );
		
		playersChoosePanel.add(showPositionButton);
		
		sumSalButton = new JButton("Sum of all players salary");
		sumSalButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  int num = -1;
				  try {
						
						
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{? = call league_pack.return_sum_of_all_salaries}"));
						orccdb.getCalStat().registerOutParameter(1, OracleTypes.NUMBER);
						orccdb.getCalStat().execute();
						num = orccdb.getCalStat().getInt(1);
						JOptionPane.showMessageDialog(this_frame,"The sum of all the saleries is "+num, "Message", JOptionPane.INFORMATION_MESSAGE);

					} catch (SQLException e1) {e1.printStackTrace();}
			  } 
			} );
		playersChoosePanel.add(sumSalButton);
		
		
		upgradeSalButton = new JButton("Upgrade salaries");
		upgradeSalButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  try {
						
						
						orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{call league_pack.upgrade_players_sal}"));
						orccdb.getCalStat().execute();

						DefaultTableModel model = (DefaultTableModel) playersTable.getModel();
						  int rowCount = model.getRowCount();
						  //Remove rows one by one from the end of the table
						  for (int i = rowCount - 1; i >= 0; i--) {
							  model.removeRow(i);
						  }
						  
						  DefaultTableModel model1 = (DefaultTableModel) playersTable.getModel();
							
							try {
								
								
								orccdb.setCallableStatement(orccdb.getConnection().prepareCall("{? = call league_pack.players_refcursor_function( ? )}"));
								orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
								orccdb.getCalStat().setString(2, getTextFromRB());
								orccdb.getCalStat().execute();
								orccdb.setResultSet(((OracleCallableStatement)orccdb.getCalStat()).getCursor(1));
								
								res = ((OracleCallableStatement)orccdb.getCalStat()).getCursor(1);
								
								while(res.next()) {
									model1.addRow(new Object[] {res.getString(2), res.getString(3), res.getString(4), 
											res.getString(5), res.getInt(6), res.getInt(7), res.getString(8)});
								}
								
								
							} catch (SQLException e1) {e1.printStackTrace();}
						
						
						
						
						JOptionPane.showMessageDialog(this_frame,"The salaries has been upgraded ", "Message", JOptionPane.INFORMATION_MESSAGE);

					} catch (SQLException e1) {e1.printStackTrace();}
			  } 
			} );
		playersChoosePanel.add(upgradeSalButton);
		
		
		
		
		addPlayerPanel.add(playersChoosePanel);
		
		addPlayerButton = new JButton("Add Player");
		addPlayerButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				new AddPlayerForm("Add Player Form", orccdb);
				this_frame.dispose();
			  } 
			} );
		addPlayerPanel.add(addPlayerButton);
		
		this.add(addPlayerPanel, BorderLayout.PAGE_END);
		
		
		this.pack();
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		this.setVisible(true);
	}
	
	private String getTextFromRB() {
		if (allCHB.isSelected()) return "All";
		else if (pgCHB.isSelected()) return "PG";
		else if (sgCHB.isSelected()) return "SG";
		else if (sfCHB.isSelected()) return "SF";
		else if (pfCHB.isSelected()) return "PF";
		else return "C";
	}
}
