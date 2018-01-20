package com.example.security.service;

import com.example.security.entity.UserToken;
import com.example.security.repository.JdbcTokenBasedSecurityContextRepository.JdbcTokenRepository;
import com.example.security.repository.UserTokenRepository;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserTokenService implements JdbcTokenRepository {

  @Autowired
  private UserTokenRepository userTokenRepository;

  @Override
  public String loadUsername(String token) {
    UserToken userToken = userTokenRepository.findByToken(token);
    return Optional.ofNullable(userToken).map(UserToken::getUsername).orElse(null);
  }

  @Override
  public void saveToken(String token, SecurityContext context, long tokenExpired) {
    UserToken userToken = Optional.ofNullable(userTokenRepository.findByToken(token)).orElseGet(UserToken::new);
    userToken.setToken(token);
    userToken.setUsername(context.getAuthentication().getName());
    userToken.setExpiredAt(System.currentTimeMillis() + tokenExpired * 60 * 1_000);
    UserToken oldUserToken = userTokenRepository.findByUsername(context.getAuthentication().getName());
    if (oldUserToken != null && !oldUserToken.getId().equals(userToken.getId())) {
      userTokenRepository.delete(oldUserToken);
      log.info("用户{}异地登录，清除无效的历史登录", oldUserToken.getUsername());
    }
    userTokenRepository.save(userToken);
  }

  @Override
  public boolean invalidToken(String token) {
    return userTokenRepository.deleteByToken(token);
  }

  @Override
  public int invalidExpiredToken() {
    return userTokenRepository.deleteByExpiredAtLessThan(System.currentTimeMillis());
  }
}
