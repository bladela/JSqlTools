package swing.workers;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;

import datasource.driver.DatasourceDriverInt;
import gui.frames.TabPanel;
import gui.tables.models.ResultQueryTableModel;

public class DbConnectionSwingWorker extends SwingWorker<ResultQueryTableModel, Void> {
	private List<String> sql;
	private TabPanel panel;
	private DatasourceDriverInt driver;
	private ResourceBundle rb;
		
	
	public DbConnectionSwingWorker(TabPanel panel, DatasourceDriverInt driver)
	{
		this.panel=panel;
		this.driver=driver;

	}
	
	public void setRB(ResourceBundle rb)
	{
		this.rb=rb;
		driver.setRB(rb);
	}
	
	public DatasourceDriverInt getDriver()
	{
		return driver;
	}
	
	@Override
    protected ResultQueryTableModel doInBackground() {
		ResultQueryTableModel model = new ResultQueryTableModel();
		model.setRB(rb);
		Object[] columnNames=null;
		Object[][] datas=null;
		System.out.println("background");
		for (String query:sql)
		{
			try
			{
				List<Map<String, Object>> rowData = driver.executeQuery(query);
				if(rowData.size()==0)
		        {	
		        	 columnNames=new Object[]{rb.getString("result")};
		        	 datas=new Object[][]{{rb.getString("nodatafound")}};
		        	 model.setColumnNames(columnNames);
		        }
		        else
		        {
		        	columnNames=rowData.get(0).keySet().toArray();
		        	model.setColumnNames(columnNames);
		        	datas=new Object[rowData.size()][model.getColumnCount()];
					Object[] row=new Object[model.getColumnCount()];
					int i=0,j=0;
					/*for (Object columnName:columnNames)
					{
						row[j]=columnName;
						j++;
					}*/
					//datas[i++]=row.clone();
					for(Map<String,Object> riga: rowData)
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
		        }
			}
			catch (SQLException e)
			{
				 columnNames=new Object[]{rb.getString("sql.execution.error")};
	        	 datas=new Object[][]{{e.getSQLState()+"-"+e.getErrorCode()+e.getMessage()}};
	        	 try
	        	 {
	        		 driver.rollback();
	        	 }
	        	 catch (SQLException ex)
	        	 {
	        		 ex.printStackTrace();
	        	 }
	        	 model.setColumnNames(columnNames);
			}
			
	        
	
	        
	        
	        
	        
			model.setData(datas);
		}
        return model;
    }
	
	@Override
    protected void done() {
       try{
        	AbstractTableModel model = get();
            panel.updateTableData(model);
        } catch (ExecutionException | InterruptedException ex) {
            ex.printStackTrace();
        }
        
    }
	
	public void setSql(String sql)
	{
		this.sql=Arrays.asList(sql.split(";"));
	}
	
	public void disconnect()
	{
		driver.disconnect();
	}
	
	public List<String> getReservedWords()
	{
		return driver.getReservedWords();
	}
	
	public List<String> getObjWords()
	{
		return driver.getObjWords();
	}

}

