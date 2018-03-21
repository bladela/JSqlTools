package gui.windows;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JEditorPane;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JTabbedPane;
import javax.swing.JFormattedTextField;
import javax.swing.JTextPane;
import javax.swing.plaf.metal.MetalTabbedPaneUI;

import datasource.type.impl.Postgresql;
import datasource.util.DatasourceLoginInfo;
import gui.events.ConnectionButtonMouseListener;
import gui.events.MainWindowEventAdapter;
import gui.frames.TabPanel;
import jsyntaxpane.DefaultSyntaxKit;

import javax.swing.JTable;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
	List<TabPanel> panel_2=null;

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
		btnTest.addMouseListener(new ConnectionButtonMouseListener());
		panel_1.add(btnTest);

		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(5, 5));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		panel.add(tabbedPane, BorderLayout.CENTER);

		panel_2 = new ArrayList<TabPanel>();
		TabPanel firstPanel=new TabPanel(basicWidth, basicHeight, new Postgresql(),tabbedPane);
		firstPanel.setDbLoginInfo(new DatasourceLoginInfo("bladela","satana","agenda","springagenda","5432","localhost"));
		panel_2.add(firstPanel);
		
		
		tabbedPane.add("<html><body><table width='100'><tr><td>New Tab</td></tr></table></body></html>",  panel_2.get(0));
		
		

		tabbedPane.addKeyListener(new TabActionListener(tabbedPane));

		tabbedPane.setUI(new CustomTabbedPaneUI());
		

		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		createMenu(menuBar);

		frame.addWindowListener(new MainWindowEventAdapter(tabbedPane));



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

class TabActionListener implements KeyListener
{
	JTabbedPane panel=null;

	public TabActionListener(JTabbedPane p)
	{
		this.panel=p;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub



	}



	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if ((e.getKeyCode() == KeyEvent.VK_N) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			System.out.println("woot!");
			panel.addTab("<html><body><table width='100'><tr><td><b>New Tab</b></td></tr></table></body></html>",new TabPanel(panel.getWidth(), panel.getHeight(), new Postgresql(),panel));
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	//private


}

class CustomTabbedPaneUI extends MetalTabbedPaneUI
{
   Rectangle xRect;
  
   protected void installListeners() {
      super.installListeners();
      tabPane.addMouseListener(new MyMouseHandler());
      
   }
  
   protected void paintTab(Graphics g, int tabPlacement,
                           Rectangle[] rects, int tabIndex,
                           Rectangle iconRect, Rectangle textRect) {
      super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
      
      Font f = g.getFont();
      g.setFont(new Font("Courier", Font.BOLD, 10));
      FontMetrics fm = g.getFontMetrics(g.getFont());
      int charWidth = fm.charWidth('x');
      int maxAscent = fm.getMaxAscent();
      g.drawString("x", textRect.x + textRect.width - 3, textRect.y + textRect.height - 3);
      g.drawRect(textRect.x+textRect.width-5,
                 textRect.y+textRect.height-maxAscent, charWidth+2, maxAscent-1);
      xRect = new Rectangle(textRect.x+textRect.width-5,
                 textRect.y+textRect.height-maxAscent, charWidth+2, maxAscent-1);
      g.setFont(f);
      
    }
  
    public class MyMouseHandler extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            //System.out.println(e);
            if (xRect.contains(e.getPoint())) {
               JTabbedPane tabPane = (JTabbedPane)e.getSource();
               int tabIndex = tabForCoordinate(tabPane, e.getX(), e.getY());

               if(tabPane.getComponentAt(tabIndex)!=null)
            	   ((TabPanel)tabPane.getComponentAt(tabIndex)).disconnect();
               tabPane.remove(tabIndex);
            }
        }
    }
    
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        return 25; // manipulate this number however you please.
    }
    
    protected int calculateTabWidth(int tabPlacement, int tabIndex, int fontHeight) {
        return 20; // manipulate this number however you please.
    }
}
