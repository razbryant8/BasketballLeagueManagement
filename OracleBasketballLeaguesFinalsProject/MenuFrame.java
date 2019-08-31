package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import databaseConnection.OracleConnectionDB;

public class MenuFrame extends JFrame{
	private final int HEIGHT= 600, WIDTH = 600;
	private OracleConnectionDB orccdb;
	private JButton arenasButton, playersButton, teamsButton, leaguesButton;
	private JPanel menuPanel;
	
	public MenuFrame(String name, OracleConnectionDB orccdb) throws Exception {
		this.setTitle(name);
		this.orccdb = orccdb;
		
	//	this.orccdb.printTableByName("teams");
		menuPanel = new JPanel(new GridLayout(4, 1));
		
		leaguesButton = new JButton("Leagues");
		leaguesButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				new LeaguesFrame("Leagues", orccdb);
			  } 
			} );
		menuPanel.add(leaguesButton);
		
		teamsButton = new JButton("Teams");
		teamsButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				new TeamsFrame("Teams", orccdb);
			  } 
			} );
		menuPanel.add(teamsButton);
		
		playersButton = new JButton("Players");
		playersButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				new PlayersFrame("Players", orccdb);
			  } 
			} );
		menuPanel.add(playersButton);
		
		arenasButton = new JButton("Arenas");
		arenasButton.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				new ArenasFrame("Arenas", orccdb);
			  } 
			} );
		menuPanel.add(arenasButton);
		
		this.add(menuPanel);
		
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
}
