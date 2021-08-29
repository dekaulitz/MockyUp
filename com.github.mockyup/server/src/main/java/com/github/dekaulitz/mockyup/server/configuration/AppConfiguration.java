package com.github.dekaulitz.mockyup.server.configuration;

import static io.lettuce.core.ReadFrom.REPLICA_PREFERRED;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class AppConfiguration {

  @Value("${com.github.dekaulitz.mockyup.redis.host}")
  private String redisHost;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    String[] host = redisHost.split(":");
    LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
        .readFrom(REPLICA_PREFERRED)
        .build();

    return new LettuceConnectionFactory(
        new RedisStandaloneConfiguration(host[0], Integer.parseInt(host[1])), clientConfig);
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate(
      RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(RedisSerializer.string());
    template.setValueSerializer(RedisSerializer.json());
    return template;
  }

}
