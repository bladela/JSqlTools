package main;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import datasource.driver.DatasourceDriverInt;
import datasource.driver.impl.PostgresDriver;
import gui.windows.MainWindow;

public class JSQLTools {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*DatasourceDriverInt driver=new PostgresDriver();
		driver.setUsername("bladela");
		driver.setPassword("satana");
		driver.setUrl("localhost");
		driver.setPort("5432");
		driver.setDatabase("springagenda");
		driver.connect();
		try
		{
			//List results=driver.executeQuery("insert into contact(nome,cognome,cod_fiscale,version,contact_id) values('Andrea','Chiari','ABCED',1,40)".trim());
			//List results=driver.executeQuery("select * from contact A".trim());
			List results=driver.executeQuery("create table Ajeje ( prova integer)".trim());
			for (int i=0;i<results.size();i++)
			{
				System.out.println(((Map)results.get(i)).get("nome")+","+((Map)results.get(i)).get("cognome"));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
			
		
		driver.disconnect();*/
		MainWindow window=new MainWindow();
		window.setVisible(true);
		
	}

}
