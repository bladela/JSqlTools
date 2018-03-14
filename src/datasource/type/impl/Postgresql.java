package datasource.type.impl;

import datasource.type.DatasourceTypeInt;

public class Postgresql implements DatasourceTypeInt {

	@Override
	public String getDatasourceName() {
		// TODO Auto-generated method stub
		return "PostgreSQL";
	}

	@Override
	public Integer getDatasourceId() {
		// TODO Auto-generated method stub
		return new Integer(1);
	}

}
