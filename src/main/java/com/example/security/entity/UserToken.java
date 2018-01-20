package com.example.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "t_user_token")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserToken extends AbstractEntity {

  @Column(name = "f_username", nullable = false)
  private String username;
  @Column(name = "f_token", nullable = false, updatable = false)
  private String token;
  @Column(name = "f_expired_at", nullable = false)
  private Long expiredAt;
}
