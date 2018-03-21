package datasource.util;

public class DatasourceLoginInfo {
	private String username;
	private String password;
	private String database;
	private String schema;
	private String port;
	private String url;
	
	public DatasourceLoginInfo(String username, String password, String schema, String database, String port, String url)
	{
		this.username=username;
		this.password=password;
		this.schema=schema;
		this.port=port;
		this.url=url;
		this.database=database;		
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
