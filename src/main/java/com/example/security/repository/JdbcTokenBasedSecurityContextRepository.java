package com.example.security.repository;

import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpRequestResponseHolder;

/**
 * 基于jdbc的令牌{@link org.springframework.security.web.context.SecurityContextRepository}
 */
@Setter
@Getter
@Slf4j
public class JdbcTokenBasedSecurityContextRepository extends TokenBasedSecurityContextRepository implements
    InitializingBean {

  /**
   * 默认的过期token周期，单位秒
   */
  public static final int DEFAULT_INVALID_TOKEN_PERIOD = 60;
  private JdbcTokenRepository tokenRepository;
  private UserDetailsService userDetailsService;
  /**
   * token过期周期
   */
  private int invalidTokenPeriod = DEFAULT_INVALID_TOKEN_PERIOD;

  @Override
  public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
    String token = loadTokenValue(requestResponseHolder.getRequest());
    if (StringUtils.isBlank(token)) {
      return SecurityContextHolder.createEmptyContext();
    }
    String username = tokenRepository.loadUsername(token);
    if (StringUtils.isBlank(username)) {
      log.debug("令牌{}已失效", token);
      return SecurityContextHolder.createEmptyContext();
    }
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    SecurityContext securityContext = new SecurityContextImpl();
    securityContext.setAuthentication(
        new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities()));
    return securityContext;
  }

  @Override
  protected void saveContextInternal(SecurityContext context, HttpServletRequest request,
      HttpServletResponse response) {
    tokenRepository.saveToken(loadTokenValue(request), context, getTokenExpired());
  }

  @Override
  public boolean containsContext(HttpServletRequest request) {
    String token = loadTokenValue(request);
    if (token == null) {
      return false;
    }
    return StringUtils.isNotBlank(tokenRepository.loadUsername(token));
  }

  @Override
  public void afterPropertiesSet() {
    if (invalidTokenPeriod <= 0) {
      log.debug("未启用定时失效过期token定时器");
      return;
    }
    Timer timer = new Timer("token-checker", true);
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        int invalidCount = tokenRepository.invalidExpiredToken();
        if (invalidCount > 0) {
          log.info("失效{}个过期token成功", invalidCount);
        }
      }
    }, 0, invalidTokenPeriod * 1_000);
    log.debug("启用定时失效过期token定时器，执行间隔:{}秒", invalidTokenPeriod);
  }

  public interface JdbcTokenRepository {

    /**
     * 根据token获取用户名
     *
     * @param token 令牌
     * @return null 如果token不存在
     */
    String loadUsername(String token);

    /**
     * 保存token，一般是保存token,username,tokenExpired
     *
     * @param token token
     * @param context context
     * @param tokenExpired token有效期，单位分钟
     */
    void saveToken(String token, SecurityContext context, long tokenExpired);

    /**
     * 使token无效
     *
     * @param token 令牌
     * @return true:操作成功;false:操作失败
     */
    boolean invalidToken(String token);

    /**
     * 使过期的token无效
     *
     * @return 过期的token数量
     */
    int invalidExpiredToken();
  }
}
