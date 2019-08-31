package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import databaseConnection.*;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

public class ArenasFrame extends JFrame{
	private final int HEIGHT= 600, WIDTH = 800;
	private OracleConnectionDB orccdb;
	private JTable arenasTable;
	private JPanel arenaPanel,addArenaPanel;
	private JButton addArenaButton;
	private ResultSet res;
	private JFrame this_frame;
	
	public ArenasFrame(String name, OracleConnectionDB orccdb) {
		this.setTitle(name);
		this.orccdb = orccdb;
		this.setLayout(new BorderLayout());
		this_frame = this;
		
		arenaPanel = new JPanel(new BorderLayout());
		
		arenasTable = new JTable(new DefaultTableModel(null,new Object[]{"Arena Name","City","Number Of Seats"}));
		DefaultTableModel model = (DefaultTableModel) arenasTable.getModel();
		
		try {
			
			
			this.orccdb.setCallableStatement(this.orccdb.getConnection().prepareCall("{? = call league_pack.arenas_refcursor_function}"));
			this.orccdb.getCalStat().registerOutParameter(1, OracleTypes.CURSOR);
			this.orccdb.getCalStat().execute();
			this.orccdb.setResultSet(((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1));
			
			res = ((OracleCallableStatement)this.orccdb.getCalStat()).getCursor(1);
			
			while(res.next()) {
				model.addRow(new Object[] {res.getString(2), res.getString(3), res.getInt(4)});
			}
			
			
		} catch (SQLException e) {e.printStackTrace();}
		
		
		arenasTable.setEnabled(false);
		arenaPanel.add(arenasTable.getTableHeader(),BorderLayout.PAGE_START);
		arenaPanel.add(new JScrollPane(arenasTable));
		arenaPanel.add(arenasTable,BorderLayout.CENTER);
		
		this.add(arenaPanel, BorderLayout.CENTER);
		
		
		
		addArenaPanel = new JPanel();
		addArenaPanel.setLayout(new GridLayout(1, 1));
		
		addArenaButton = new JButton("Add Arena");
		addArenaButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				new AddArenaForm("Add Arena Form", orccdb);
				this_frame.dispose();
			  } 
			} );
		addArenaPanel.add(addArenaButton);
		
		this.add(addArenaPanel, BorderLayout.PAGE_END);
		
		
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		this.setVisible(true);
	}
}
