package com.example.security.repository;

import com.example.security.entity.UserToken;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

  UserToken findByToken(String token);

  UserToken findByUsername(String username);

  boolean deleteByToken(String token);

  @Transactional
  int deleteByExpiredAtLessThan(long expiredAt);
}
