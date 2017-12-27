package com.ceiba.repository.nosqldb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface IDbNoSql {


  public <T> Boolean save(T entity) throws Exception;


  public <T> T findOne(Serializable primaryKey, Class<T> entityClass);


  public <T> void delete(Serializable primaryKey, Class<T> entityClass);


  public <T> boolean exists(Serializable primaryKey, Class<T> entityClass) throws Exception;


  public <T> List<T> getAllObjects(Class<T> entityClass) throws Exception;

  
  public <T> T findOneByFieldValue(String field, String value, Class<T> entityClass) throws Exception;

  
  public <T> List<T> findByFieldValues(Map<String, String> fields, Class<T> entityClass) throws Exception;

  public <T> Boolean saveOrUpdate(T entity) throws Exception;
}
