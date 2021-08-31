package com.github.dekaulitz.mockyup.server.service.common.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.dekaulitz.mockyup.server.service.common.api.CacheService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public <T> T findCacheByKey(String key, Class<T> clazz) {
    String value = redisTemplate.opsForValue().get(key);
    if (StringUtils.isEmpty(value)) {
      return null;
    }
    try {
      return objectMapper.readValue(value, clazz);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      log.error("findCacheByKey: Exception: ", e);
      return null;
    }
  }

  @Override
  public <T> List<T> findCacheListByKey(String key, Class<T> clazz) {
    String value = redisTemplate.opsForValue().get(key);
    if (StringUtils.isEmpty(value)) {
      return null;
    }
    try {
      final CollectionType listType = objectMapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, clazz);
      return objectMapper.readValue(value, listType);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      log.error("findCacheListByKey failed ex:{} key:{}", e, key);
      return null;
    }
  }

  @Override
  public void createCache(String key, Object value, long expirySeconds) {
    if (value == null) {
      return;
    }
    try {
      String valueString = objectMapper.writeValueAsString(value);
      redisTemplate.opsForValue().set(key, valueString, expirySeconds, TimeUnit.SECONDS);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      log.error("createCache failed ex:{} key:{}", e, key);
    }
  }

  @Override
  public void deleteCache(String key) {
    try {
      redisTemplate.delete(key);
    } catch (Exception e) {
      e.printStackTrace();
      log.error("deleteCache failed ex:{} key:{}", e, key);
    }
  }

  @Override
  public void deleteCacheByPrefixKey(String key) {
    Set<String> keys = redisTemplate.keys(key);
    if (!CollectionUtils.isEmpty(keys)) {
      keys.forEach(s -> {
        try {
          redisTemplate.delete(key);
        } catch (Exception e) {
          e.printStackTrace();
          log.error("deleteCacheByPrefixKey failed ex:{} key:{}", e, key);
        }
      });
    }
  }
}
