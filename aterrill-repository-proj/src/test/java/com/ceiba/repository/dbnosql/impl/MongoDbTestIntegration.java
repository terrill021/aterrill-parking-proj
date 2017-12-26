package com.ceiba.repository.dbnosql.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.ceiba.repository.dbnosql.model.Tester;
import com.ceiba.repository.nosqldb.IDbNoSql;
import com.ceiba.repository.nosqldb.config.MongoDbConnection;
import com.ceiba.repository.nosqldb.impl.MongoDb;
import com.ceiba.repository.nosqldb.model.DataSourceNoSql;

public class MongoDbTestIntegration {

	private MongoTemplate mongoTemplate;

	@InjectMocks
	private MongoDb mongoDb;

	MongoDbConnection mongoDbConnection;
	private Tester tester;

	/**
	 * This method makes all the previous configuration necessary to realize the
	 * tests.
	 */
	@Before
	public void sepUp() throws Exception {
		mongoDbConnection = new MongoDbConnection() {
			@Override
			public DataSourceNoSql getDataSourceNoSql() {
				// TODO Auto-generated method stub
				DataSourceNoSql dataSourceNoSql = new DataSourceNoSql();
				dataSourceNoSql.setDataBase("test-mongo");
				dataSourceNoSql.setHost("127.0.0.1");
				dataSourceNoSql.setPort(27017);
				return dataSourceNoSql;
			}

			@Override
			public IDbNoSql iDbNoSql() {
				// TODO Auto-generated method stub
				return null;
			}
		};

		tester = new Tester();
		mongoDb = new MongoDb();
		mongoTemplate = mongoDbConnection.mongoTemplate();
		mongoDb.setMongoTemplate(mongoTemplate);
	}

	@Test
	public void saveTest() throws Exception {
		// arrange
		// act
		Boolean res = mongoDb.save(tester);
		// assert
		Assert.assertTrue(res);
	}

	@Test(expected = Exception.class)
	public void saveFailTest() throws Exception {
		// arrange
		// act
		mongoDb.save(null);
		// assert
		Assert.fail();
	}

}
