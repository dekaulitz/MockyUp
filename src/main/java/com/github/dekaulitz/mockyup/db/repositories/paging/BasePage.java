package com.github.dekaulitz.mockyup.db.repositories.paging;

import org.springframework.data.mongodb.core.MongoTemplate;


interface BasePage<T> {

  void addCriteria(String q);

  void setConnection(MongoTemplate mongoTemplate);
}
