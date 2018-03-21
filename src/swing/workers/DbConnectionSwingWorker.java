package swing.workers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
	
		
	
	public DbConnectionSwingWorker(TabPanel panel, DatasourceDriverInt driver)
	{
		this.panel=panel;
		this.driver=driver;
	}
	
	public DatasourceDriverInt getDriver()
	{
		return driver;
	}
	
	@Override
    protected ResultQueryTableModel doInBackground() throws Exception {
		ResultQueryTableModel model = new ResultQueryTableModel();
		System.out.println("background");
		for (String query:sql)
		{
	        List<Map<String, Object>> rowData = driver.executeQuery(query);
	        System.out.println(query);
	        System.out.println(rowData.size());
	        Object[] columnNames=rowData.get(0).keySet().toArray();      
	
	        
	        model.setColumnNames(columnNames);
	        
	        Object[][] datas=new Object[rowData.size()+1][model.getColumnCount()];
			Object[] row=new Object[model.getColumnCount()];
			int i=0,j=0;
			for (Object columnName:columnNames)
			{
				row[j]=columnName;
				j++;
			}
			datas[i++]=row.clone();
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

