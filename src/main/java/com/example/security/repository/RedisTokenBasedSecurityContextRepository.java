package com.example.security.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;

/**
 * 基于redis的令牌{@link org.springframework.security.web.context.SecurityContextRepository}
 */
@Getter
@Setter
public class RedisTokenBasedSecurityContextRepository extends TokenBasedSecurityContextRepository implements
    InitializingBean {

  private RedisTemplate<String, String> redisTemplate;
  private TokenKeyGenerator tokenKeyGenerator;
  private ObjectMapper objectMapper;

  @Override
  public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
    String token = loadTokenValue(requestResponseHolder.getRequest());
    if (token == null) {
      return SecurityContextHolder.createEmptyContext();
    }
    String json = redisTemplate.opsForValue().get(tokenKeyGenerator.generate(token));
    if (json == null) {
      return SecurityContextHolder.createEmptyContext();
    }
    SecurityContext context;
    try {
      context = objectMapper.readValue(json, SecurityContext.class);
    } catch (IOException e) {
      throw new IllegalStateException("无法反序列化SecurityContext", e);
    }
    return context;
  }

  @Override
  protected void saveContextInternal(SecurityContext context, HttpServletRequest request,
      HttpServletResponse response) {
    String token = loadTokenValue(request);
    String json;
    try {
      json = objectMapper.writeValueAsString(context);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("无法序列化" + context.getClass(), e);
    }
    redisTemplate.opsForValue().set(tokenKeyGenerator.generate(token), json, getTokenExpired(), TimeUnit.SECONDS);
  }

  @Override
  public boolean containsContext(HttpServletRequest request) {
    String token = loadTokenValue(request);
    if (token == null) {
      return false;
    }
    return redisTemplate.hasKey(tokenKeyGenerator.generate(token));
  }

  @Override
  public void afterPropertiesSet() {
    tokenKeyGenerator = Optional.ofNullable(tokenKeyGenerator).orElseGet(DefaultTokenKeyGenerator::new);
  }

  /**
   * token key 生成器
   */
  public interface TokenKeyGenerator {

    /**
     * 生成token key
     */
    String generate(String token);
  }

  public static class DefaultTokenKeyGenerator implements TokenKeyGenerator {

    private final String prefix;

    public DefaultTokenKeyGenerator() {
      this("security");
    }

    public DefaultTokenKeyGenerator(String prefix) {
      if (StringUtils.isBlank(prefix)) {
        this.prefix = prefix;
      } else {
        this.prefix = prefix.endsWith(":") ? prefix : (prefix + ":");
      }
    }

    @Override
    public String generate(String token) {
      return prefix + token;
    }
  }
}
