package gui.tables.models;

import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

public class ResultQueryTableModel extends AbstractTableModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object rowData[][]=null;
	private Object columnNames[]=null;
	public ResourceBundle rb;
	
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }
    
    public void setRB(ResourceBundle rb)
    {
    	this.rb=rb;
    }
    
    
    public void setColumnNames(Object[] columnNames)
    {
    	Object names[]=new Object[columnNames.length+1];
    	names[0]=rb.getString("table.column.header.riga");
    	for (int i=1;i<names.length;i++)
    		names[i]=columnNames[i-1];
    	this.columnNames=names;
    	this.fireTableDataChanged();
    }
    
    public void setData(Object[][] data)
    {
    	Object table[][]=new Object[data.length][data[0].length+1];
    	for (int i=0;i<data.length;i++)
    	{
    		for(int j=0;j<data[0].length+1;j++)
    		{
    			if(j==0&&i>=0)
    				table[i][0]=(i+1);
    			if((j!=0&&i>=0))
    				table[i][j]=data[i][j-1];
    		}
    	}
    	/*rowData=null;
    	rowData=data;*/
    	rowData=table;
    	this.fireTableDataChanged();  	
    	
    }
    
    
    public int getRowCount() { return rowData.length; }
    public int getColumnCount() { return columnNames.length; }
    public Object getValueAt(int row, int col) {
        return rowData[row][col];
    }
    public boolean isCellEditable(int row, int col)
        { return false; }
    
    public void setValueAt(Object value, int row, int col) {
        rowData[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}

