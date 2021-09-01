package com.github.dekaulitz.mockyup.server.configuration.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

@Configuration
@EnableConfigurationProperties
public class MongoConfiguration extends AbstractMongoClientConfiguration {

  @Value("${com.github.dekaulitz.mockyup.mongodb.database.name}")
  private String databaseName;

  @Value("${com.github.dekaulitz.mockyup.mongodb.database.host}")
  private String host;

  @Override
  protected String getDatabaseName() {
    return databaseName;
  }

  public MongoCustomConversions customConversions() {
    List<Converter<?, ?>> converters = new ArrayList<>();
//    converters.add(new LocalTimeToStringConverter());
//    converters.add(new StringToLocalTimeConverter());
    converters.add(new ObjectNodeToMapConverter());
    return new MongoCustomConversions(converters);
  }

  @Override
  public MongoClient mongoClient() {
    ConnectionString connectionString = new ConnectionString(host);
    MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .build();

    return MongoClients.create(mongoClientSettings);
  }

  @Override
  public MongoTemplate mongoTemplate(MongoDatabaseFactory databaseFactory,
      MappingMongoConverter converter) {
    MongoTemplate mongoTemplate = new MongoTemplate(databaseFactory, converter);
    MappingMongoConverter conv = (MappingMongoConverter) mongoTemplate.getConverter();
    // tell mongodb to use the custom converters
    conv.setCustomConversions(customConversions());
    conv.afterPropertiesSet();
    return mongoTemplate;
  }
}
