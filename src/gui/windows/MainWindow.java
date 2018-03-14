package gui.windows;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JEditorPane;

import java.awt.FlowLayout;
import javax.swing.JTabbedPane;
import javax.swing.JFormattedTextField;
import javax.swing.JTextPane;

import datasource.type.impl.Postgresql;
import gui.frames.TabPanel;
import jsyntaxpane.DefaultSyntaxKit;

import javax.swing.JTable;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.JTextArea;

public class MainWindow {

	private JFrame frame;
	
	private int basicWidth=589;
	private int basicHeight=470;
	JPanel panel=null;
	JPanel panel_1=null;
	JTabbedPane tabbedPane=null;
	TabPanel panel_2=null;
	JMenuBar menuBar=null;
	
	
	public void setVisible(Boolean visible)
	{
		this.frame.setVisible(visible);
	}
	
	public Boolean getVisible()
	{
		return this.frame.isVisible();
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, basicWidth, basicHeight);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, BorderLayout.NORTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnTest = new JButton("Test1");
		panel_1.add(btnTest);
		
		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(5, 5));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane, BorderLayout.CENTER);
		
		panel_2 = new TabPanel(basicWidth, basicHeight, new Postgresql());
		tabbedPane.addTab("New tab", null, panel_2, null);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		createMenu(menuBar);
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		        public void windowClosing(WindowEvent winEvt) {
		        	for(int i=0;i<tabbedPane.getTabCount();i++)
		        	{
		        		TabPanel tab=((TabPanel)tabbedPane.getComponentAt(i));
		        		if(tab!=null)
		        			tab.disconnect();
		        	}
		        	System.exit(0);
		        }
		       
		    });
		
		
		
	}
	
	private void createMenu(JMenuBar menuBar)
	{
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int i=0;i<tabbedPane.getTabCount();i++)
	        		((TabPanel)tabbedPane.getTabComponentAt(i)).disconnect();
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);
	}
	

}
