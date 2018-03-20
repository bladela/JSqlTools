package gui.tables.models;

import javax.swing.table.AbstractTableModel;

public class ResultQueryTableModel extends AbstractTableModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object rowData[][]=null;
	private Object columnNames[]=null;
	
    public String getColumnName(int col) {
        return columnNames[col].toString();
    }
    
    
    public void setColumnNames(Object[] columnNames)
    {
    	this.columnNames=columnNames;
    	this.fireTableDataChanged();
    }
    
    public void setData(Object[][] data)
    {
    	rowData=null;
    	rowData=data;  
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

