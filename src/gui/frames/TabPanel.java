package gui.frames;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import datasource.driver.DatasourceDriverInt;
import datasource.driver.impl.PostgresDriver;
import datasource.type.DatasourceTypeInt;
import jsyntaxpane.DefaultSyntaxKit;
import jsyntaxpane.util.StringUtils;
import util.style.KeywordStyledDocument;

public class TabPanel extends JSplitPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	JTextPane textArea=null;
	DatasourceDriverInt driver=null;
	StyleContext styleContext=null;
	Style defaultStyle=null;
	Style cwStyle=null;
	Style objStyle=null;
	
	public TabPanel(int width, int height, DatasourceTypeInt dbType)
	{
		super(JSplitPane.VERTICAL_SPLIT);
		//this.setSize(width, height);
		setDatasource(dbType);
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
		
		//this.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_3.setPreferredSize(new Dimension(500, 30));
		
		FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
		//this.add(panel_3, BorderLayout.SOUTH);
		this.setBottomComponent(panel_3);
		this.setDividerSize(3);
		this.setDividerLocation(200);
		
		
		table = new JTable();
		panel_3.add(table);
		
		JPanel panel_4 = new JPanel();
		panel_4.setSize(width, height-50);
		//this.add(panel_4, BorderLayout.CENTER);
		this.setTopComponent(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		
		
		//DefaultSyntaxKit.initKit();
		
		textArea = new JTextPane(styledDocument);
		textArea.addKeyListener(new DetectSql(this));
		//textArea.setContentType("text/html");
		JScrollPane scrPane = new JScrollPane(textArea);
		//textArea.setContentType("text/sql");
		panel_4.add(scrPane, BorderLayout.CENTER);
		
		
	}
	
	private void setDatasource(DatasourceTypeInt dbType)
	{
		if(dbType.getDatasourceName().equals("PostgreSQL"))
		{
			driver=new PostgresDriver();
			driver.setUsername("bladela");
			driver.setPassword("satana");
			driver.setUrl("localhost");
			driver.setPort("5432");
			driver.setDatabase("springagenda");
			driver.connect();
		}
	}
	
	public void disconnect()
	{
		driver.disconnect();
	}
	
	protected String getText()
	{
		return textArea.getText();
	}
	
	protected Boolean dbContainObject(String name)
	{
		return driver.containsObject(name);
	}
	
	protected void highlightDbObjects(String text)
	{
		textArea.setText(text);
	}
	
	protected Boolean isDbReserverWord(String text)
	{
		return driver.isDbReservedWord(text);
	}
	
	public void executeFullText() 
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
		
		
	}
	
	private void updateTable(List<Map<String,Object>> data)
	{
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
			panel.executeFullText();
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

class ResultQueryTableModel extends AbstractTableModel
{
	private Object rowData[][]=null;
	private Object columnNames[]=null;
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }
    
    public void setColumnNames(Object[] columnNames)
    {
    	this.columnNames=columnNames;
    }
    
    public void setData(Object[][] data)
    {
    	rowData=data;    	
    }
    
    
    public int getRowCount() { return rowData.length; }
    public int getColumnCount() { return columnNames.length; }
    public Object getValueAt(int row, int col) {
        return rowData[row][col];
    }
    public boolean isCellEditable(int row, int col)
        { return true; }
    
    public void setValueAt(Object value, int row, int col) {
        rowData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}