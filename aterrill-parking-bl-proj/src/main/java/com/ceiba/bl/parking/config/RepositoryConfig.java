package com.ceiba.bl.parking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.ceiba.repository.nosqldb.IDbNoSql;
import com.ceiba.repository.nosqldb.config.MongoDbConnection;
import com.ceiba.repository.nosqldb.impl.MongoDb;
import com.ceiba.repository.nosqldb.model.DataSourceNoSql;

@Configuration
@PropertySource("classpath:aplication.properties")
public class RepositoryConfig extends MongoDbConnection{

	@Autowired
    private Environment env;

	@Bean
	public IDbNoSql iDbNoSql() {		
		return new MongoDb();
	}
	
    public String readProperty(String propertyName) {
        
    	return env.getProperty(propertyName);
    }
    
	@Override
	public DataSourceNoSql getDataSourceNoSql() {
		
		DataSourceNoSql dataSourceNoSql = new DataSourceNoSql();
		dataSourceNoSql.setHost(readProperty("repository.ip"));
		dataSourceNoSql.setPort(Integer.parseInt(readProperty("repository.port")));
		dataSourceNoSql.setDataBase(readProperty("repository.name"));
		
		return dataSourceNoSql;
	}

}
