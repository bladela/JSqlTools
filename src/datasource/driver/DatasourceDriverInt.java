package datasource.driver;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import datasource.type.DatasourceTypeInt;

public interface DatasourceDriverInt {
	
	public void setDatasourceType(DatasourceTypeInt type);
	
	public DatasourceTypeInt getDatasourceType();
	
	public void setDatabase(String name);
	
	public String getDatabase();
	
	public void connect ();
	
	public void connect(String url, String port, String username, String password, String schema, String database) throws SQLException;
	
	public void setUsername(String username);
	
	public void setPassword(String password);
	
	public void setUrl(String url);
	
	public void setPort(String port);
	
	public void setSchema(String schema);
	
	public String getPort();
	
	public String getUrl();
	
	public String getUsername();
	
	public String getPassword();
	
	public String getSchema();
	
	public void disconnect();
	
	public void commit() throws SQLException;
	
	public void rollback() throws SQLException;
	
	public List<Map<String, Object>> executeQuery(String sql) throws SQLException;
	
	public Boolean containsObject(String name);
	public Boolean isDbReservedWord(String word);
	
	public List<String> getReservedWords();
	
	public List<String> getObjWords();
	
	public void populateObjects(String schema) throws SQLException;
	
	public void setRB(ResourceBundle rb);

	

}
