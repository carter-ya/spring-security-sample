package com.example.security.repository;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * 基于token的{@link SecurityContextRepository}
 */
@Setter
@Getter
public abstract class TokenBasedSecurityContextRepository implements SecurityContextRepository {

  public static final String DEFAULT_HEADER_TOKEN_NAME = "Token";
  public static final String DEFAULT_PARAMETER_TOKEN_NAME = "token";
  /**
   * 默认的token过期时间，单位分钟
   */
  public static final int DEFAULT_TOKEN_EXPIRED = 30 * 60;

  private String headerTokenName = DEFAULT_HEADER_TOKEN_NAME;
  private String parameterTokenName = DEFAULT_PARAMETER_TOKEN_NAME;
  private long tokenExpired = DEFAULT_TOKEN_EXPIRED;
  private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

  @Override
  public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
    // 无授权信息
    if (context.getAuthentication() == null) {
      return;
    }
    // 匿名用户
    if (trustResolver.isAnonymous(context.getAuthentication())) {
      return;
    }
    // 无有效的token
    if (StringUtils.isBlank(loadTokenValue(request))) {
      return;
    }
    saveContextInternal(context, request, response);
  }

  /**
   * 保存{@link SecurityContext}，默认情况下，如果是匿名用户，则不调用该方法
   */
  protected abstract void saveContextInternal(SecurityContext context, HttpServletRequest request,
      HttpServletResponse response);

  /**
   * 从请求中读取token
   */
  protected String loadTokenValue(HttpServletRequest request) {
    String value = request.getHeader(headerTokenName);
    return Optional.ofNullable(value).orElse(request.getParameter(parameterTokenName));
  }

}
