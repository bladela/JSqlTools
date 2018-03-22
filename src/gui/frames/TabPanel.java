package gui.frames;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import datasource.driver.DatasourceDriverInt;
import datasource.driver.impl.PostgresDriver;
import datasource.type.DatasourceTypeInt;
import datasource.type.impl.Postgresql;
import datasource.util.DatasourceLoginInfo;
import gui.tables.models.ResultQueryTableModel;
import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.util.StringUtils;
import swing.workers.DbConnectionSwingWorker;
import util.style.KeywordStyledDocument;

public class TabPanel extends JSplitPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	JTextPane textArea=null;
	private DbConnectionSwingWorker driver=null;
	StyleContext styleContext=null;
	Style defaultStyle=null;
	Style cwStyle=null;
	Style objStyle=null;
	JTabbedPane parent=null;
	private DatasourceLoginInfo dbLoginInfo=null;
	DatasourceTypeInt dbType=null;
	public final ResourceBundle rb;
	JPanel panelText;
	
	
	
	public TabPanel(int width, int height, DatasourceTypeInt dbType, JTabbedPane parent,ResourceBundle rb)
	{
		super(JSplitPane.VERTICAL_SPLIT);
		this.parent=parent;
		//this.setSize(width, height);
		//setDatasource(dbType);
		this.dbType=dbType;
		this.rb=rb;
		
		//this.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_3.setPreferredSize(new Dimension(500, 30));
		
		BorderLayout borderLayout = new BorderLayout(0,0);
		panel_3.setLayout(borderLayout);
		//this.add(panel_3, BorderLayout.SOUTH);
		this.setBottomComponent(panel_3);
		this.setDividerSize(3);
		this.setDividerLocation(200);
		
		
		table = new JTable();
		JScrollPane pane=new JScrollPane(table);
		panel_3.add(pane,BorderLayout.CENTER);
		
		panelText = new JPanel();
		panelText.setSize(width, height-50);
		//this.add(panel_4, BorderLayout.CENTER);
		this.setTopComponent(panelText);
		panelText.setLayout(new BorderLayout(0, 0));
		
		
		//DefaultSyntaxKit.initKit();
		
		
		
		
	}
	
	public void setDbLoginInfo(DatasourceLoginInfo info)
	{
		this.dbLoginInfo=info;
		setDatasource(this.dbType);
	}
	
	private void setDatasource(DatasourceTypeInt dbType)
	{
		DatasourceDriverInt dbDriver=null;
		if(dbType.getDatasourceName().equals("PostgreSQL"))
		{
			dbDriver=new PostgresDriver();
			dbDriver.setUsername(dbLoginInfo.getUsername());
			dbDriver.setPassword(dbLoginInfo.getPassword());
			dbDriver.setUrl(dbLoginInfo.getUrl());
			dbDriver.setPort(dbLoginInfo.getPort());
			dbDriver.setDatabase(dbLoginInfo.getDatabase());
			dbDriver.setSchema(dbLoginInfo.getSchema());
			dbDriver.connect();
			driver=new DbConnectionSwingWorker(this,dbDriver);
			driver.setRB(rb);
			initFormat();
			
		}
	}
	
	private void initFormat()
	{
		styleContext = new StyleContext();
        defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
        cwStyle = styleContext.addStyle("ConstantWidth", null);
        objStyle = styleContext.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(cwStyle, Color.BLUE);
        StyleConstants.setBold(cwStyle, true);
        StyleConstants.setForeground(objStyle, Color.RED);
        StyleConstants.setBold(objStyle, true);
        KeywordStyledDocument styledDocument=new KeywordStyledDocument(defaultStyle, cwStyle,objStyle);
        KeywordStyledDocument.setReservedWords(driver.getReservedWords());
        KeywordStyledDocument.setObjWords(driver.getObjWords());
        
        textArea = new JTextPane(styledDocument);
		textArea.addKeyListener(new DetectSql(this));
		//textArea.setContentType("text/html");
		JScrollPane scrPane = new JScrollPane(textArea);
		//textArea.setContentType("text/sql");
		panelText.add(scrPane, BorderLayout.CENTER);
	}
	
	
	
	
	
	public void disconnect()
	{
		driver.disconnect();
	}
	
	protected String getText()
	{
		return textArea.getText();
	}
	
	
	
	protected void highlightDbObjects(String text)
	{
		textArea.setText(text);
	}
	
	
	/*public void executeFullText() 
	{
		String text=this.getText();
		List<String> queries=Arrays.asList(text.split(";"));
		for(String query: queries)
		{
			query=query.trim();
			System.out.println(query);
			try {
				List<Map<String,Object>> data=driver.executeQuery(query);
				updateTable(data);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}*/
	
	private void updateTable(List<Map<String,Object>> data)
	{
		//table= new JTable();
		Object [] columnNames=data.get(0).keySet().toArray();
		ResultQueryTableModel model=new ResultQueryTableModel();
		model.setColumnNames(columnNames);
		Object[][] datas=new Object[data.size()+1][model.getColumnCount()];
		Object[] row=new Object[model.getColumnCount()];
		int i=0,j=0;
		for (Object columnName:columnNames)
		{
			row[j]=columnName;
			j++;
		}
		datas[i++]=row.clone();
		for(Map<String,Object> riga: data)
		{
			j=0;
			
			for (Object columnName:columnNames)
			{
				if(riga.get(columnName)!=null)
					row[j]=riga.get(columnName).toString();
				else
					row[j]="";
				j++;
			}
			datas[i++]=row.clone();
			
		}
		model.setData(datas);
		table.setModel(model);
		
	}
	
	public void updateTableData(AbstractTableModel model)
	{
		((ResultQueryTableModel)model).setRB(rb);
		table.setModel(model);	
		TableColumnModel columns=table.getColumnModel();
		TableColumn numRiga=columns.getColumn(0);
		numRiga.setPreferredWidth((int)(table.getWidth()*0.1f));
		
	}
	
	public void notifyNewTab()
	{
		TabPanel panel=new TabPanel(this.getWidth(), this.getHeight(), new Postgresql(),parent,this.rb);
		panel.setDbLoginInfo(this.dbLoginInfo);
		parent.addTab(rb.getString("tab.defaulttitle"),panel);
	}
	
	public DbConnectionSwingWorker getDatasource()
	{
		return this.driver;
	}
	
	public DbConnectionSwingWorker getDriver()
	{
		return driver;
	}
	
	protected DbConnectionSwingWorker resetSwingWorker()
	{
		driver=new DbConnectionSwingWorker(this,driver.getDriver());
		driver.setRB(rb);
		return driver;
	}

}

class DetectSql implements KeyListener
{
	TabPanel panel=null;
	
	
	public DetectSql(TabPanel p)
	{
		this.panel=p;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		List<String> words=new ArrayList();
		/*if()
		{
			words=detectDbObjects();
		}*/
		
		
	}
	
	

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode()==e.VK_F5)
		{
			System.out.println("key pressed : F5");
			System.out.println("text :"+panel.getText());
			panel.resetSwingWorker();
			panel.getDriver().setSql(panel.getText());
			panel.getDriver().execute();
			//panel.executeFullText();
		}
		if ((e.getKeyCode() == KeyEvent.VK_N) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
            System.out.println("woot!");
            panel.notifyNewTab();
        }
			
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	//private
	
	private List<String> detectDbObjects()
	{
		/*String text=panel.getText();
		List<String> alreadyParsed=new ArrayList<String>();
		*/List<String>words=Arrays.asList(panel.getText().split("\\ +"));
		//dbObjects
		/*System.out.println("Words : "+(words.size()-1));
		for (int i =0; i<words.size();i++)
		{
			String word=words.get(i).trim();
			if(panel.dbContainObject(word)&&!alreadyParsed.contains(word))
			{
				System.out.println(word+" is a db object!!!!!!!");
				//text=text.replaceAll(word, "<i>"+word+"</i>");
			}
			
			/*if(panel.isDbReserverWord(word)&&!alreadyParsed.contains(word))
			{
				System.out.println(word+" is a reserved word!!!!!!!");
				text=text.replaceAll(word, "<b>"+word+"</b>");
			}*/
		/*	alreadyParsed.add(word);
			
		}
		
		
		
		panel.highlightDbObjects(text);
		*/
		return words;
	}
	
	
	
}

