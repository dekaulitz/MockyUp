package com.github.dekaulitz.mockyup.server.service.common.api;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface CacheService {

  <T> T findCacheByKey(String key, Class<T> clazz);

  <T> List<T> findCacheListByKey(String key, Class<T> clazz);

  Object createCache(String key, Object value, long expirySeconds);

  void deleteCache(String key);

  void deleteCacheByPrefixKey(String key);
}

