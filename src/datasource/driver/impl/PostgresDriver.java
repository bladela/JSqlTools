package datasource.driver.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import datasource.driver.DatasourceDriverInt;
import datasource.type.DatasourceTypeInt;
import gui.frames.TabPanel;
import gui.tables.models.ResultQueryTableModel;

public class PostgresDriver implements DatasourceDriverInt  {

	String username;
	String password;
	String port;
	String url;
	String database;
	String schema;
	DatasourceTypeInt datasourceType;
	List<Map<String, Object>> objects=null;
	private static String sqlWords[]={"select","from","where","group", "by","create", "table","update","on","delete","drop","index","replace","trigger",
			"alter","declare","if","while","end","insert","into","values","loop","drop","view","sequence","user","execute", "order"};
	
	Connection conn;
	
	
	
	

	@Override
	public void setDatasourceType(DatasourceTypeInt type) {

		this.datasourceType=type;
	}

	@Override
	public DatasourceTypeInt getDatasourceType() {

		return this.datasourceType;
	}

	@Override
	public void connect() {
		
		conn = null;
		String connectionUrl="jdbc:postgresql://"+url+":"+port+"/"+database;
		if(schema!=null)
			connectionUrl+="?currentSchema="+schema;
		try {
			conn = DriverManager.getConnection(connectionUrl, username, password);
			conn.setAutoCommit(false);
			populateObjects();
			System.out.println("Numero oggetti rilevati : "+objects.size());
			System.out.println("Connected to the PostgreSQL server successfully.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void connect(String url, String port, String username, String password, String schema, String database) throws SQLException {

		conn = null;
		this.url=url;
		this.port=port;
		this.username=username;
		this.password=password;
		this.schema=schema;
		this.database=database;
		String connectionUrl="jdbc:postgresql://"+url+":"+port+"/"+database;
		if(schema!=null)
			connectionUrl+="?searchpath="+schema;
		conn = DriverManager.getConnection(connectionUrl, username, password);
		conn.setAutoCommit(false);
		populateObjects();
		System.out.println("Connected to the PostgreSQL server successfully.");

	}

	@Override
	public void setUsername(String username) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPassword(String password) {
		this.password=password;
	}

	@Override
	public void setUrl(String url) {
		this.url=url;
	}

	@Override
	public void setPort(String port) {
		this.port=port;
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		try {
			conn.close();
			System.out.println("Disconnect operation successfull");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getPort() {
		return this.port;
	}

	@Override
	public String getUrl() {
		return this.url;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override 
	public void setSchema(String schema)
	{
		this.schema=schema;
	}

	@Override
	public String getSchema()
	{
		return this.schema;
	}

	@Override
	public List<Map<String, Object>> executeQuery(String sql) throws SQLException {
		// TODO Auto-generated method stub
		List<Map<String, Object>> results=new ArrayList<Map<String, Object>>();	


		//Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		if(sql.toUpperCase().startsWith("SELECT"))
			results=executeSelect(sql);
		else if(sql.toUpperCase().startsWith("INSERT")||sql.toUpperCase().startsWith("UPDATE")||sql.toUpperCase().startsWith("DELETE"))
			results=executeUpdate(sql);
		else
			results=executeDDL(sql);





		return results;
	}

	@Override
	public void setDatabase(String name) {
		// TODO Auto-generated method stub
		this.database=name;
	}

	@Override
	public String getDatabase() {
		// TODO Auto-generated method stub
		return this.database;
	}

	//private
	private List<Map<String, Object>> executeSelect(String sql) throws SQLException
	{
		int riga=0;
		List<Map<String, Object>> results=new ArrayList<Map<String, Object>>();
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmt.executeQuery(sql);

		if(!rs.last())
			throw new SQLException("ResultSet.last failed"); 
		int numRighe = rs.getRow();
		rs.beforeFirst();
		ResultSetMetaData rsmd = rs.getMetaData();
		while (riga<numRighe){
			Map<String, Object> resultRow=new LinkedHashMap<String,Object>();
			rs.next();
			for(int i=1;i<=rsmd.getColumnCount();i++)
				resultRow.put(rsmd.getColumnName(i),rs.getObject(rsmd.getColumnName(i)));
			results.add(resultRow);
			riga++;
		}

		return results;
	}

	private List<Map<String, Object>> executeUpdate(String sql) throws SQLException
	{
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		stmt.executeUpdate(sql);
		List<Map<String, Object>> results=new ArrayList<Map<String, Object>>();
		Map<String,Object> numRighe=new HashMap<String,Object>();
		numRighe.put("Righe aggiornate",stmt.getUpdateCount());
		results.add(numRighe);
		return results;
	}

	private List<Map<String, Object>> executeDDL(String sql) throws SQLException
	{
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
		List<Map<String, Object>> results=new ArrayList<Map<String, Object>>();

		Map<String,Object> numRighe=new HashMap<String,Object>();
		numRighe.put("risultato","Successo");
		results.add(numRighe);
		return results;
	}

	private void populateObjects() throws SQLException
	{
		/*tables*/

		populateObjects(schema);

	}
	
	public void populateObjects(String schema) throws SQLException
	{
		String sql="select * from pg_catalog.pg_tables where schemaname='"+schema+"'";
		objects=executeSelect(sql);
		
	}

	public Boolean containsObject(String name)
	{
		/*in tables*/
		for (int i=0;i<objects.size();i++)
		{
			Map<String, Object> object=objects.get(i);
			if(object.get("tablename")!=null&&object.get("tablename").equals(name))				
				return true;
		}

		return false;
	}

	@Override
	public Boolean isDbReservedWord(String word)
	{
		for (int i=0;i<sqlWords.length;i++)
		{
			if(word.equals(sqlWords[i]))
				return true;
		}
		return false;
	}

	public List<String> getReservedWords()
	{
		return Arrays.asList(sqlWords);
	}

	public List<String> getObjWords()
	{
		List<String> obj=new ArrayList<String>();
		for(int i=0;i<objects.size();i++)
			obj.add((String)objects.get(i).get("tablename"));
		return obj;

	}

	






}
