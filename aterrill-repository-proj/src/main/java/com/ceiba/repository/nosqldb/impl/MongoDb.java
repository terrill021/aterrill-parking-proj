package com.ceiba.repository.nosqldb.impl;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.ceiba.repository.nosqldb.IDbNoSql;

@Service
public class MongoDb implements IDbNoSql {

  @Autowired
  private MongoTemplate mongoTemplate;

  public void setMongoTemplate(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public <T> Boolean save(T entity) throws Exception {
	  
      mongoTemplate.insert(entity);
      return Boolean.TRUE;
  }
  
  public <T> Boolean saveOrUpdate(T entity) throws Exception {
	  
      mongoTemplate.save(entity);
      return Boolean.TRUE;
  }

  public <T> List<T> getAllObjects(Class<T> type) {
      return mongoTemplate.findAll(type);
   
  }

  public <T> T findOne(Serializable primaryKey, Class<T> type) {
      return mongoTemplate.findOne(new Query(Criteria.where("_id").is(primaryKey)), type);
    
  }

  public <T> void delete(Serializable primaryKey, Class<T> type) {
      mongoTemplate.remove(new Query(Criteria.where("_id").is(primaryKey)), type);   
  }

  public <T> boolean exists(Serializable primaryKey, Class<T> type) throws Exception {
      return mongoTemplate.exists(new Query(Criteria.where("_id").is(primaryKey)), type);
  }

  public <T> T findOneByFieldValue(String field, String value, Class<T> type) {
      return mongoTemplate.findOne(new Query(Criteria.where(field).is(value)), type);
  }

  public <T> List<T> findByFieldValues(Map<String, String> fields, Class<T> type) {

    Criteria queryTemp = new Criteria();

    if (fields == null || fields.isEmpty()) {
      return null;
    }

    Iterator<Map.Entry<String, String>> it = fields.entrySet().iterator();
    Map.Entry<String, String> pair = it.next();
    queryTemp = Criteria.where(pair.getKey()).regex(pair.getValue());

    // Recorrer el resto
    while (it.hasNext()) {
      pair = it.next();
      queryTemp = queryTemp.and(pair.getKey()).regex(pair.getValue());
    }
      return mongoTemplate.find(new Query(queryTemp), type);
    
  }  

}
